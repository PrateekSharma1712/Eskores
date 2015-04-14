package com.prateek.eskores;

/**
 * Created by prateek on 17/3/15.
 */
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by prateek on 17/3/15.
 */
public class NetworkManager {

    private static NetworkManager _manager = null;
    private NetworkListener listener = null;

    private NetworkManager() {

    }

    public static NetworkManager getInstance() {
        if(_manager == null) {
            _manager = new NetworkManager();
        }

        return _manager;
    }

    public void load(NetworkListener listener) {
        this.listener = listener;
        new DownloadXmlTask().execute("http://static.cricinfo.com/rss/livescores.xml");
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, List<ScoreFeed>> {

        @Override
        protected List<ScoreFeed> doInBackground(String... params) {
            try {
                return loadXmlFromNetwork(params[0]);
            } catch (IOException e) {
                return null;
            } catch (XmlPullParserException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ScoreFeed> scoreFeeds) {
            super.onPostExecute(scoreFeeds);
            if(listener != null) {
                listener.onResponse(scoreFeeds);
            }
        }
    }

    // downloads XML from score site, parses it
    private List<ScoreFeed> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        ScoreParser scoreParser = ScoreParser.getInstance();
        List<ScoreFeed> entries = null;

        try {
            stream = ScoreParser.downloadUrl(urlString);
            entries = scoreParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return entries;
    }

}
