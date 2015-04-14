package com.prateek.eskores;

import java.util.List;

/**
 * Created by prateek on 17/3/15.
 */
public interface NetworkListener {

    public void onResponse(List<ScoreFeed> scores);
}
