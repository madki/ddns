package xyz.madki.ddns.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import xyz.madki.ddns.MainActivity;
import xyz.madki.ddns.R;

/**
 * Created by madki on 24/12/15.
 */
public class NotificationHelper {

    public static void showNotification(String message, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int color = ContextCompat.getColor(context, R.color.colorPrimary);

        PendingIntent intent = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentTitle("Dyn DNS")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(color)
                .setContentIntent(intent);

        notificationManager.notify(2, builder.build());
    }

}
