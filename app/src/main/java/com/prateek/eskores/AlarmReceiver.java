package com.prateek.eskores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by prateek on 17/3/15.
 */
public class AlarmReceiver extends BroadcastReceiver implements NetworkListener{

    private Context context = null;
    private Intent htmlLoadIntent = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        htmlLoadIntent = new Intent(context, HtmlLoadService.class);
        DebugLogger.message("onReceive");
        if(EskoreApp.isConnected(context))
            NetworkManager.getInstance().load(this);
        else {
            DebugLogger.message("Eskores : Are you connected to network?");
            Toast.makeText(context, "Eskores : Are you connected to network?", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(List<ScoreFeed> scores) {

        List<ScoreFeed> toNotifyScores = new ArrayList<>();

        for(ScoreFeed feed : scores) {
            String[] split = feed.getTitle().split("v");
            String first = split[0].replaceAll("[\\d\\*]", "").replace("/", "").replace("&", "");
            String second = split[1].replaceAll("[\\d\\*]", "").replace("/", "").replace("&", "");

            Set<String> teamsSelected = Preferences.getPreference(Preferences.SELECTED_TEAMS);
            if(teamsSelected != null && (teamsSelected.contains(first.trim()) || (teamsSelected.contains(second.trim())))) {
                //NotificationManager.notifyUser(context, feed);
                toNotifyScores.add(feed);
            }
        }

        ArrayList<ScoreFeed> toNotifyScoresArrayList = new ArrayList<>(toNotifyScores);
        htmlLoadIntent.putParcelableArrayListExtra("scores", toNotifyScoresArrayList);
        htmlLoadIntent.putExtra("source", "alarm_receiver");
        context.startService(htmlLoadIntent);
    }
}
