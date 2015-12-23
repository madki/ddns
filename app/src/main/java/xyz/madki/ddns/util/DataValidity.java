package xyz.madki.ddns.util;

import android.text.TextUtils;

/**
 * Created by madki on 24/12/15.
 */
public class DataValidity {

    public static boolean isDataValid(String host, String domain, String password) {
        return !TextUtils.isEmpty(host)
                && !TextUtils.isEmpty(domain)
                && !TextUtils.isEmpty(password);
    }

}
