package com.prateek.eskores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, NetworkListener, CustomDialog.OnDialogButtonClickListener{

    private TextView start = null;
    private TextView stop = null;
    private Toolbar mToolbar = null;
    Intent settingsIntent = null;
    private ViewPager pager = null;
    private MatchesPagerAdapter pagerAdapter = null;
    private SlidingTabLayout mSlidingTabLayout;
    public static List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();
    private List<ScoreFeed> preferredScores = null;
    private List<ScoreFeed> remainingScores = null;
    private List<ScoreFeed> allScores = null;

    static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;

        SamplePagerItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        /**
         * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
         */
        Fragment createFragment(Object tag) {
            return MatchesFragment.newInstance(mTitle, mIndicatorColor, mDividerColor, tag);
        }

        /**
         * @return the title which represents this tab. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        CharSequence getTitle() {
            return mTitle;
        }

        /**
         * @return the color to be used for indicator on the {@link SlidingTabLayout}
         */
        int getIndicatorColor() {
            return mIndicatorColor;
        }

        /**
         * @return the color to be used for right divider on the {@link SlidingTabLayout}
         */
        int getDividerColor() {
            return mDividerColor;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(EskoreApp.isConnected(this))
            NetworkManager.getInstance().load(this);
        else
            Toast.makeText(this, "Eskores : Are you connected to network?", Toast.LENGTH_SHORT).show();

        start = (TextView) findViewById(R.id.start);
        stop = (TextView) findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        DebugLogger.message("PREFERENCE IS STARTED "+Preferences.getPreferenceBoolean(Preferences.IS_STARTED));
        if(Preferences.getPreferenceBoolean(Preferences.IS_STARTED)) {
            setStarted(true);
        } else {
            setStarted(false);
        }

        pager = (ViewPager) findViewById(R.id.viewpager);

        mToolbar = (Toolbar) findViewById(R.id.vToolbar);
        mToolbar.setLogo(getResources().getDrawable(R.drawable.ic_notification));
        setSupportActionBar(mToolbar);

        settingsIntent = new Intent(this, SettingsActivity.class);

        mTabs.add(new SamplePagerItem(
                MatchesPagerAdapter.PREF_MATCHES, // Title
                R.color.theme_default_primary_dark, // Indicator color
                R.color.theme_default_primary_light // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                MatchesPagerAdapter.OTHER_MATCHES, // Title
                R.color.theme_default_primary_dark, // Indicator color
                R.color.theme_default_primary_light // Divider color
        ));

        pagerAdapter = new MatchesPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(pager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Preferences.getPreferenceBoolean(Preferences.IS_STARTED)) {
            setStartText();
        }
        if(allScores != null) {
            onResponse(allScores);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(settingsIntent);
        } else if(id == R.id.info) {
            openInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openInfo() {
        CustomDialog.getInstance(this).show(null,getString(R.string.info_message), getString(R.string.feedback), null, CustomDialog.INFO_DIALOG,this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(start)) {
            if(Preferences.getPreference(Preferences.SELECTED_TEAMS) != null && Preferences.getPreference(Preferences.SELECTED_TEAMS).size() > 0) {
                if(preferredScores.size() > 0 ) {
                    EskoreApp.getInstance().setAlarm();
                    Preferences.savePreference(Preferences.IS_STARTED, true);
                    setStarted(true);
                }else {
                    CustomDialog.getInstance(this).show(null,getString(R.string.no_preference_live_matches_message), getString(android.R.string.ok), null, CustomDialog.NO_PREFERENCE_LIVE_MATCH_DIALOG,this);
                }
            } else {
                CustomDialog.getInstance(this).show(null,getString(R.string.no_preference_message), getString(R.string.change_settings), getString(android.R.string.cancel), CustomDialog.NO_PREFERENCE_TEAM_DIALOG,this);
            }
        } else if(v.equals(stop)) {
            Preferences.savePreference(Preferences.IS_STARTED, false);
            setStarted(false);
            EskoreApp.getInstance().cancelAlarm();
            NotificationManager.stop(this);
            EskoreApp.notificationMap.clear();
        }
    }

    @Override
    public void onResponse(List<ScoreFeed> scores) {

        preferredScores = new ArrayList<>();
        remainingScores = new ArrayList<>();
        allScores = scores;
        if(scores.size() > 0 && scores.get(0).getTitle().equals("No Match in progress..")) {

        } else {
            for (ScoreFeed feed : scores) {
                String[] split = feed.getTitle().split("v");
                String first = split[0].replaceAll("[\\d\\*]", "").replace("/", "").replace("&", "");
                String second = split[1].replaceAll("[\\d\\*]", "").replace("/", "").replace("&", "");

                Set<String> teamsSelected = Preferences.getPreference(Preferences.SELECTED_TEAMS);
                if (teamsSelected != null && (teamsSelected.contains(first.trim()) || (teamsSelected.contains(second.trim())))) {
                    preferredScores.add(feed);
                } else {
                    remainingScores.add(feed);
                }
            }
        }
        populateList(new ArrayList<ScoreFeed>(preferredScores), new ArrayList<ScoreFeed>(remainingScores));
    }

    public void populateList(List<ScoreFeed> preferredScores, List<ScoreFeed> remainingScores) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        DebugLogger.message(fragments);
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (((MatchesFragment) fragment).getFragmentTag().equals(MatchesPagerAdapter.PREF_MATCHES)) {
                    ((MatchesFragment) fragment).updateMatches(preferredScores);
                } else if (((MatchesFragment) fragment).getFragmentTag().equals(MatchesPagerAdapter.OTHER_MATCHES)) {
                    ((MatchesFragment) fragment).updateMatches(remainingScores);
                }
            }
      }
    }

    @Override
    public void onDialogPositiveButtonClicked(String dialogType) {
        if(dialogType.equals(CustomDialog.NO_PREFERENCE_TEAM_DIALOG)) {
            startActivity(settingsIntent);
        } else if(dialogType.equals(CustomDialog.NO_PREFERENCE_LIVE_MATCH_DIALOG)) {

        } else if(dialogType.equals(CustomDialog.INFO_DIALOG)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.prateek.eskores"));
            startActivity(intent);
        }
    }

    @Override
    public void onDialogNegativeButtonClicked() {

    }

    private void setStarted(boolean started) {
        if(started) {
            start.setText("STARTED");
            stop.setText("STOP\nNOTIFICATION");
            activate(stop);
            deActivate(start);
        } else {
            stop.setText("STOPPED");
            setStartText();
            deActivate(stop);
            activate(start);
        }
    }

    private void activate(TextView view) {
        view.setClickable(true);
        view.setTextColor(getResources().getColor(R.color.theme_default_primary_dark));
    }

    private void deActivate(TextView view) {
        view.setClickable(false);
        view.setTextColor(getResources().getColor(R.color.theme_default_primary_light));
    }

    private void setStartText() {
        start.setText("START\nNOTIFICATIONS");
        String refreshMinute = String.valueOf(Preferences.getPreferenceLong(Preferences.DELAY_DURATION) / Preferences.MINUTE);
        start.setText(start.getText() + "\nRefresh in "+ refreshMinute + ((refreshMinute.equals("1")) ? "minute" :" minutes"));
        int selectedTeamSize = Preferences.getPreference(Preferences.SELECTED_TEAMS).size();

        if(Preferences.getPreference(Preferences.SELECTED_TEAMS) != null && selectedTeamSize > 0) {
            start.setText(start.getText() + "\nfor "+ selectedTeamSize + ((selectedTeamSize > 1) ? " teams" :" team"));
        }
    }

}
