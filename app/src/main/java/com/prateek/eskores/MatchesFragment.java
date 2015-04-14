package com.prateek.eskores;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by prateek on 22/3/15.
 */
public class MatchesFragment extends Fragment{

    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";

    private ListView matchesList = null;
    private ProgressBar loading = null;
    private TextView message = null;
    private MatchesAdapter adapter = null;
    private Object tag = null;

    @Override
    public String toString() {
        return "MatchesFragment{" +
                "tag=" + tag +
                '}';
    }

    public static MatchesFragment newInstance(CharSequence title, int indicatorColor,
                                              int dividerColor, Object tag) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);

        MatchesFragment fragment = new MatchesFragment();
        fragment.tag = tag;
        fragment.setArguments(bundle);

        return fragment;
    }

    public Object getFragmentTag() {
        return tag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.matches_fragment, container, false);
        matchesList = (ListView) rootView.findViewById(R.id.matchesList);
        loading = (ProgressBar) rootView.findViewById(R.id.loading);
        message = (TextView) rootView.findViewById(R.id.message);
        return rootView;
    }

    public void updateMatches(List<ScoreFeed> scores) {
        if(matchesList != null) {
            if(scores.size() == 0) {
                matchesList.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                message.setText("No matches");
            } else {
                if (adapter == null) {
                    adapter = new MatchesAdapter(this.getActivity());
                }
                adapter.updateList(scores);
                matchesList.setAdapter(adapter);
                matchesList.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                message.setVisibility(View.GONE);
            }
        }
    }
}
