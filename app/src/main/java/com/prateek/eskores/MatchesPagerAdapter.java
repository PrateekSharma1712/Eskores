package com.prateek.eskores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by prateek on 22/3/15.
 */
public class MatchesPagerAdapter extends FragmentPagerAdapter {

    public static final String PREF_MATCHES = "Preferred Matches";
    public static final String OTHER_MATCHES = "Other Matches";
    private FragmentManager fragmentManager = null;

    public MatchesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1: return MainActivity.mTabs.get(position).createFragment(OTHER_MATCHES);

            case 0:
            default:return MainActivity.mTabs.get(position).createFragment(PREF_MATCHES);
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0: title = PREF_MATCHES;
                break;

            case 1: title = OTHER_MATCHES;
                break;
        }
        return title;
    }
}
