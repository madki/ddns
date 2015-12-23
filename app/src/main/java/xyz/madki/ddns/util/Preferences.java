package xyz.madki.ddns.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by madki on 24/12/15.
 */
public class Preferences {
    private SharedPreferences preferences;

    public Preferences(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("default.prefs", Context.MODE_PRIVATE);
    }

    public void setHost(String host) {
        preferences.edit().putString(Keys.HOST, host).apply();
    }

    public String getHost(String defaultVal) {
        return preferences.getString(Keys.HOST, defaultVal);
    }

    public void setDomain(String domain) {
        preferences.edit().putString(Keys.DOMAIN, domain).apply();
    }

    public String getDomain(String defaultVal) {
        return preferences.getString(Keys.DOMAIN, defaultVal);
    }

    public void setPassword(String password) {
        preferences.edit().putString(Keys.PASSWORD, password).apply();
    }

    public String getPassword(String defaultVal) {
        return preferences.getString(Keys.PASSWORD, defaultVal);
    }

    public void setLastIp(String lastIp) {
        preferences.edit().putString(Keys.LAST_IP, lastIp).apply();
    }

    public String getLastIp(String defaultVal) {
        return preferences.getString(Keys.LAST_IP, defaultVal);
    }

    private static final class Keys {
        public static final String HOST = "host";
        public static final String DOMAIN = "domain";
        public static final String PASSWORD = "password";
        public static final String LAST_IP = "last_ip";

        private Keys() {
            throw new AssertionError("No instances");
        }
    }
}
