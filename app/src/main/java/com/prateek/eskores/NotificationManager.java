package com.prateek.eskores;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by prateek on 17/3/15.
 */
public class NotificationManager {

    private static Uri path = Uri.parse("android.resource://com.prateek.eskores/" + R.raw.ball_bounce);


    public static void notifyUser(Context context, ScoreFeed feed) {
        if (context == null) {
            DebugLogger.message("Invalid context, user can not be notified with local notification.");
            return;
        }

        if(feed != null) {

            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(context, feed.getId(),
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            String desc = EskoreApp.formatDesc(feed); //((feed.getOversPlayed() == null) ? "" : (", " + feed.getOversPlayed()));
            String title = EskoreApp.formatTitle(feed.getTitle());
            DebugLogger.message("feed"+feed);

            StringBuilder descTemp = new StringBuilder(desc);
            if(feed.getBat1() != null) {
                descTemp.append("\n\n");
                descTemp.append(feed.getBat1());
                descTemp.append("\n");
            }
            if(feed.getBat2() != null) {
                descTemp.append(feed.getBat2());
                descTemp.append("\n");
            }
            if(feed.getBowl1() != null) {
                descTemp.append("\n");
                descTemp.append(feed.getBowl1());
            }

            desc = descTemp.toString();



            // build notificationAlarmManager
            Notification notify = new NotificationCompat.Builder(context)
                    .setContentTitle(title).setContentText(desc)
                    .setContentIntent(pIntent)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(desc))
                    .setSound(path)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();

            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(feed.getId(), notify);

            EskoreApp.notificationMap.put(feed.getId(), notify);
        }
    }

    public static void stop(Context context) {
        Set<Map.Entry<Integer, Notification>> entrySet = EskoreApp.notificationMap.entrySet();
        for(Map.Entry<Integer, Notification> entry : entrySet) {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(entry.getKey());
        }
    }

    public static void stop(Context context, int notificationId) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

}
