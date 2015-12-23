package xyz.madki.ddns;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import xyz.madki.ddns.util.ApiUtils;
import xyz.madki.ddns.util.IpUtils;
import xyz.madki.ddns.util.Preferences;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.et_host)
    EditText host;
    @Bind(R.id.et_domain)
    EditText domain;
    @Bind(R.id.et_password)
    EditText password;
    @Bind(R.id.tv_ip)
    TextView ipHolder;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    NameCheapClient client;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        client = ApiUtils.setUpNameCheapClient();
        preferences = new Preferences(this);

        fillFromPrefs();

        ipHolder.setText(IpUtils.getIpAddress(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (isDataValid()) {
                    saveValues();
                    final String ip = IpUtils.getIpAddress(MainActivity.this);

                    Snackbar.make(view, "Updating ip to " + ip, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                    Observable<ResponseBody> updateIp = client.updateIp(host.getText().toString(),
                            domain.getText().toString(),
                            password.getText().toString(),
                            ip);

                    updateIp.subscribeOn(Schedulers.io())
                            .subscribe(new Observer<ResponseBody>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Snackbar.make(view, "Please check network connection", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                                @Override
                                public void onNext(ResponseBody responseBody) {
                                    boolean success = false;
                                    try {
                                        String responseStr = responseBody.string();
                                        Timber.d(responseStr);
                                        if (responseStr.contains("<Done>true</Done>")) {
                                            preferences.setLastIp(ip);
                                            Snackbar.make(view, "Updated ip successfully", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            success = true;
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (!success) {
                                        Snackbar.make(view, "Some error occurred", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                }
                            });
                } else {
                    Snackbar.make(view, "Empty fields!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

    }

    private void fillFromPrefs() {
        String lastHost = preferences.getHost(null);
        String lastDomain = preferences.getDomain(null);
        String lastPassword = preferences.getPassword(null);

        if (lastHost != null) host.setText(lastHost);
        if (lastDomain != null) domain.setText(lastDomain);
        if (lastPassword != null) password.setText(lastPassword);
    }

    private void saveValues() {
        preferences.setHost(host.getText().toString());
        preferences.setDomain(domain.getText().toString());
        preferences.setPassword(password.getText().toString());
    }

    private boolean isDataValid() {
        return !TextUtils.isEmpty(host.getText())
                && !TextUtils.isEmpty(domain.getText())
                && !TextUtils.isEmpty(password.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
