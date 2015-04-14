package com.prateek.eskores;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by prateek on 16/3/15.
 */
public class EskoreApp extends Application {

    public static final String TAG = EskoreApp.class
            .getSimpleName();

    private static EskoreApp mInstance;

    public static Map<Integer, Notification> notificationMap = new HashMap<Integer, Notification>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized EskoreApp getInstance() {
        return mInstance;
    }

    public void setAlarm() {
        DebugLogger.message("setAlarm");
        Context context = getApplicationContext();
        Calendar cal = Calendar.getInstance();

        Intent aIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, aIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        DebugLogger.message("DELAY_DURATION.. "+Preferences.getPreferenceLong(Preferences.DELAY_DURATION));
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), Preferences.getPreferenceLong(Preferences.DELAY_DURATION), sender);
    }

    public void cancelAlarm() {
        Context context = getApplicationContext();
        Intent aIntent = new Intent(context,AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, aIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    public static String formatDesc(ScoreFeed feed) {
        StringBuilder result = new StringBuilder();
        String[] split = feed.getDescription().split("v");
        if(split[1].contains("*")) {
            result.append((split[1] + ((feed.getOversPlayed() == null) ? "" : (", "+feed.getOversPlayed()))).trim()).append("\n").append(split[0].trim());
        } else {
            result.append((split[0] + ((feed.getOversPlayed() == null) ? "" : (", "+feed.getOversPlayed()))).trim()).append("\n").append(split[1].trim());
        }
        return result.toString();
    }

    public static String formatTitle(String title) {
        StringBuilder result = new StringBuilder();
        String[] split = title.split("v");
        String first = split[0].replaceAll("[\\d\\*]", "").replace("/", "").replace("&", "");
        String second = split[1].replaceAll("[\\d\\*]", "").replace("/", "").replace("&", "");
        result.append(first.trim()).append(" v ").append(second.trim());
        return result.toString();

    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
