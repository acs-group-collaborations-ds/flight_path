package com.example.orbitalflightpaths;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MissionOverviewActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String mname, shipn, payloadn, start_point, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_overview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MissionFromPrefs();

    }

    private void MissionFromPrefs(){
        sharedPreferences = getSharedPreferences("MissionData", MODE_PRIVATE);
        mname = sharedPreferences.getString("name", "");
        shipn = sharedPreferences.getString("ship", "");
        payloadn = sharedPreferences.getString("payload", "");
        start_point = sharedPreferences.getString("start_point", "");
        destination = sharedPreferences.getString("destination", "");
    }

    @Override
    public void onBackPressed() {
        Intent dash = new Intent(MissionOverviewActivity.this, Dashboard.class);
        startActivity(dash);
        finish();
    }


}
