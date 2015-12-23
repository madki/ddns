package xyz.madki.ddns;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import xyz.madki.ddns.util.ApiUtils;
import xyz.madki.ddns.util.DataValidity;
import xyz.madki.ddns.util.IpUtils;
import xyz.madki.ddns.util.NotificationHelper;
import xyz.madki.ddns.util.Preferences;

/**
 * Created by madki on 23/12/15.
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String ip = IpUtils.getIpAddress(context);

        if (!ip.equals("0.0.0.0")) {
            final Preferences preferences = new Preferences(context);
            String lastIp = preferences.getLastIp(null);
            if (!ip.equals(lastIp)) {
                Timber.d("new ip found");
                String host = preferences.getHost(null);
                String domain = preferences.getDomain(null);
                String password = preferences.getPassword(null);

                if (DataValidity.isDataValid(host, domain, password)) {
                    Timber.d("All credentials present");
                    NameCheapClient client = ApiUtils.setUpNameCheapClient();
                    Observable<ResponseBody> updateIp = client.updateIp(host, domain, password, ip);

                    updateIp.subscribeOn(Schedulers.io())
                            .subscribe(new Observer<ResponseBody>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    NotificationHelper.showNotification("Unable to update DNS. Network error", context);
                                }

                                @Override
                                public void onNext(ResponseBody responseBody) {
                                    boolean success = false;

                                    try {
                                        String responseStr = responseBody.string();
                                        if(responseStr.contains("<Done>true</Done>")) {
                                            success = true;
                                            preferences.setLastIp(ip);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if(!success) {
                                        NotificationHelper.showNotification("Unable to update DNS. Incorrect settings", context);
                                    }
                                }
                            });
                }
            } else {
                Timber.d("Same as old ip, skipping");
            }
        } else {
            Timber.d("No wifi ?");
        }
    }

}
