package com.prateek.eskores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;


public class SettingsActivity extends ActionBarActivity {

    private ListView preferencesList = null;
    private TeamAdapter teamAdapter = null;
    private TextView savePreferencesButton = null;
    private int minutes = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferencesList = (ListView) findViewById(R.id.preferencesList);
        savePreferencesButton = (TextView) findViewById(R.id.savePreferencesButton);

        String internationalTeams[] = getResources().getStringArray(R.array.teamsInternational);

        teamAdapter = new TeamAdapter(this, Arrays.asList(internationalTeams));
        preferencesList.setAdapter(teamAdapter);
        savePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.savePreferenceSet(Preferences.SELECTED_TEAMS, teamAdapter.getTeamSet());
                Preferences.savePreferenceString(Preferences.DELAY_DURATION, minutes * Preferences.MINUTE);
                if(Preferences.getPreferenceBoolean(Preferences.IS_STARTED)) {
                    EskoreApp.getInstance().cancelAlarm();
                    EskoreApp.getInstance().setAlarm();
                }
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openRefreshOptionsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose refresh delay");
        builder.setItems(R.array.refresh_values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0 :
                        minutes = 1;
                        break;
                    case 1 :
                        minutes = 2;
                        break;
                    case 2 :
                        minutes = 3;
                        break;
                    case 3 :
                        minutes = 5;
                        break;
                    case 4 :
                        minutes = 10;
                        break;
                    case 5 :
                        minutes = 30;
                        break;
                    case 6 :
                        minutes = 60;
                        break;
                }
                teamAdapter.updateList(String.valueOf(minutes));
            }
        });
        builder.show();
    }
}
