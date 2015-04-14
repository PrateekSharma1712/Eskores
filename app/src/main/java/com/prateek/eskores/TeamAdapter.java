package com.prateek.eskores;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by prateek on 19/3/15.
 */
public class TeamAdapter extends BaseAdapter {

    private int additionalFields;
    private static final String REFRESH_DELAY_LABEL = "Choose refresh delay";
    private static final String REFRESH_DELAY_VALUE = "Delay in updating notifications\n";
    private static final String INTERNATIONAL_LABEL = "International";
    private static final String IPL_LABEL = "IPL";
    private static final String BIG_BASH_LEAGUE = "Big Bash League";
    private static final String SOUTH_AFRICA_LEAGUE = "South Africa League";
    private static final String NEW_ZEALAND_LEAGUE = "New Zealand League";
    private static final String AUS_DOM = "Australia Domestic";

    private static final int refreshLabelPosition = 0;
    private static final int refreshValuePosition = 1;
    private static final int internationalLabelPosition = 2;
    private static final int iplLabelPosition = 13;
    private static final int bblLabelPosition = 22;
    private static final int nzlLabelPosition = 31;
    private static final int salLabelPosition = 38;
    private static final int ausDomLabelPosition = 45;

    private ArrayList<String> teams = null;
    private SettingsActivity context = null;

    private SparseBooleanArray array = new SparseBooleanArray();

    public Set<String> getTeamSet() {
        Set<String> set = new HashSet<>();
        for(int i = 0;i<array.size();i++) {
            if(array.get(i)) {
                set.add(teams.get(array.keyAt(i)));
            }
        }
        return set;
    }

    static class TeamHolder {
        private TextView teamName;
        private CheckBox check;

        TeamHolder(View view) {
            teamName = (TextView) view.findViewById(R.id.teamName);
            check = (CheckBox) view.findViewById(R.id.check);
        }
    }

    static class LabelHolder {
        private TextView labelName;

        LabelHolder(View view) {
            labelName = (TextView) view.findViewById(R.id.labelName);
        }
    }

    static class RefreshHolder {
        private TextView valueName;
        private View view;

        RefreshHolder(View view) {
            valueName = (TextView) view.findViewById(R.id.refreshText);
            this.view = view;
        }
    }

    public TeamAdapter(SettingsActivity context, List<String> teams) {
        this.context = context;
        this.teams = new ArrayList<>(teams);

        this.teams.add(refreshLabelPosition, REFRESH_DELAY_LABEL);
        this.teams.add(refreshValuePosition, getRefreshDelay());
        this.teams.add(internationalLabelPosition, INTERNATIONAL_LABEL);
        this.teams.add(iplLabelPosition, IPL_LABEL);
        this.teams.add(bblLabelPosition, BIG_BASH_LEAGUE);
        this.teams.add(nzlLabelPosition, NEW_ZEALAND_LEAGUE);
        this.teams.add(salLabelPosition, SOUTH_AFRICA_LEAGUE);
        this.teams.add(ausDomLabelPosition, AUS_DOM);

        Set<String> preSelectedTeams = Preferences.getPreference(Preferences.SELECTED_TEAMS);

        for(int i = 0; i <this.teams.size();i++) {
            if(preSelectedTeams != null && preSelectedTeams.contains(this.teams.get(i))) {
                array.put(i, true);
            } else {
                array.put(i, false);
            }
        }
    }

    public void updateList(String addition) {
        this.teams.set(refreshValuePosition, getRefreshDelay(addition));
        notifyDataSetChanged();
    }

    private String getRefreshDelay() {
        return getRefreshDelay(String.valueOf(Preferences.getPreferenceLong(Preferences.DELAY_DURATION)/Preferences.MINUTE));
    }

    private String getRefreshDelay(String addition) {
        StringBuilder builder = new StringBuilder(REFRESH_DELAY_VALUE);
        builder.append(addition).append(" minutes");
        return builder.toString();
    }

    @Override
    public int getCount() {
        return teams.size();
    }

    @Override
    public Object getItem(int position) {
        return teams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TeamHolder teamHolder;
        LabelHolder labelHolder;
        RefreshHolder refreshHolder;
        switch (position) {
            case refreshLabelPosition:
            case internationalLabelPosition:
            case iplLabelPosition:
            case bblLabelPosition:
            case nzlLabelPosition:
            case salLabelPosition:
            case ausDomLabelPosition: {
                if(view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.label_item, parent, false);
                    labelHolder = new LabelHolder(view);
                    view.setTag(labelHolder);
                } else {
                    if(!(view.getTag() instanceof LabelHolder)) {
                        view = LayoutInflater.from(context).inflate(R.layout.label_item, parent, false);
                        labelHolder = new LabelHolder(view);
                        view.setTag(labelHolder);
                    } else {
                        labelHolder = new LabelHolder(view);
                    }
                }

                labelHolder.labelName.setText(teams.get(position));

                break;
            }
            case refreshValuePosition: {
                if(view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.refresh_item, parent, false);
                    refreshHolder = new RefreshHolder(view);
                    view.setTag(refreshHolder);
                } else {
                    if(!(view.getTag() instanceof RefreshHolder)) {
                        view = LayoutInflater.from(context).inflate(R.layout.refresh_item, parent, false);
                        refreshHolder = new RefreshHolder(view);
                        view.setTag(refreshHolder);
                    } else {
                        refreshHolder = new RefreshHolder(view);
                    }
                }

                refreshHolder.valueName.setText(teams.get(position));
                refreshHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.openRefreshOptionsDialog();
                    }
                });
                break;
            }


            default: {
                if(view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.teams_item, parent, false);
                    teamHolder = new TeamHolder(view);
                    view.setTag(teamHolder);
                } else {
                    if(!(view.getTag() instanceof TeamHolder)) {
                        view = LayoutInflater.from(context).inflate(R.layout.teams_item, parent, false);
                        teamHolder = new TeamHolder(view);
                        view.setTag(teamHolder);
                    } else {
                        teamHolder = (TeamHolder) view.getTag();
                    }
                }

                teamHolder.teamName.setText(teams.get(position));
                teamHolder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(array.get(position)) {
                            array.put(position, false);
                        } else {
                            array.put(position, true);
                        }
                        DebugLogger.message(array);
                    }
                });

                if(array.get(position)) {
                    teamHolder.check.setChecked(true);
                } else {
                    teamHolder.check.setChecked(false);
                }
                break;
            }

        }

        return view;
    }


}
