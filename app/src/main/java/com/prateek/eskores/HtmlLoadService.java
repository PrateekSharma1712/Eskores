package com.prateek.eskores;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;

import com.prateek.eskores.ScoreFeed;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Source;

/**
 * Created by prateek on 18/3/15.
 */
public class HtmlLoadService extends IntentService{

    private List<ScoreFeed> scores = null;
    private String source = null;
    private Context context = null;
    private MainActivity activity = null;

    public HtmlLoadService() {
        super("Html Load Service");
        context = this;
        activity = new MainActivity();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onHandleIntent(Intent intent) {
        scores = intent.getParcelableArrayListExtra("scores");
        source = intent.getStringExtra("source");
        DebugLogger.message("from service"+scores.toString());
        for(ScoreFeed score : scores) {
            //String html = ScoreParser.downloadUrlString(score.getLink());
            Document doc = null;
            try {
                doc = Jsoup.connect(score.getLink()).get();
                String titleInfo[] = doc.title().split("-");
                StringBuilder status = new StringBuilder();
                //check for match over
                if(titleInfo.length > 1) {
                    if(titleInfo[1].toLowerCase().contains("match over")) {
                        NotificationManager.stop(context, score.getId());
                        continue;
                    }
                    int indexOfOr = titleInfo[1].indexOf("|");
                    status.append(titleInfo[1].substring(0, indexOfOr));
                }

                int indexOfOpenP = doc.title().indexOf("(");
                int indexOfCloseP = doc.title().indexOf(")");

                if(indexOfOpenP != -1) {
                    String detail[] = doc.title().substring(indexOfOpenP+1, indexOfCloseP).split(",");

                    int lengthOfDetail = detail.length;
                    if(lengthOfDetail == 4) {
                        score.setOversPlayed(detail[0] + ((status.toString().isEmpty()) ? "" : " - "+status));
                        score.setBat1(detail[1]);
                        score.setBat2(detail[2]);
                        score.setBowl1(detail[3]);
                    } else if(lengthOfDetail == 3) {
                        score.setOversPlayed(detail[0] + ((status.toString().isEmpty()) ? "" : " - "+status));
                        score.setBat1(detail[1]);
                        score.setBowl1(detail[2]);
                    }

                    NotificationManager.notifyUser(context, score);
                } else {
                    NotificationManager.stop(context, score.getId());
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
