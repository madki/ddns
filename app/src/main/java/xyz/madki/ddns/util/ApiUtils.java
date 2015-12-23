package xyz.madki.ddns.util;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import xyz.madki.ddns.NameCheapClient;

/**
 * Created by madki on 24/12/15.
 */
public class ApiUtils {

    public static NameCheapClient setUpNameCheapClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.networkInterceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(NameCheapClient.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(NameCheapClient.class);
    }

}
