package xyz.madki.ddns;

import com.squareup.okhttp.ResponseBody;

import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by madki on 24/12/15.
 */
public interface NameCheapClient {
    String BASE_URL = "https://dynamicdns.park-your-domain.com";

    @POST("update")
    Observable<ResponseBody> updateIp(@Query("host") String host,
                                      @Query("domain") String domain,
                                      @Query("password") String password,
                                      @Query("ip") String ip);
}
