package com.example.orbitalflightpaths;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences myDB;
    SharedPreferences.Editor myDB_Editor;
    DBHelper mission_db;
    TextView user_display;
    LinearLayout dashlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mission_db = new DBHelper(this);
        dashlay = findViewById(R.id.table_lay_dash);

        FloatingActionButton fab = findViewById(R.id.fab_New_Mission);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newMission = new Intent(Dashboard.this, NewMissionActivity.class);
                startActivity(newMission);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        myDB = getSharedPreferences("ORBITAL_DATA", MODE_PRIVATE);
        String user = myDB.getString("username", "error");
        user_display = headerView.findViewById(R.id.user_display_email);
        user_display.setText(user);
        LoadMissions();
    }

    private void LoadMissions(){
        int entries = (int)(long) mission_db.countTableItems("missions");
        final String nameArr[] = new String[entries];
        String startArr[] = new String[entries];
        String destArr[] = new String[entries];
        if (entries > 0){
            Cursor nameC1 = mission_db.readData("missions", "name");
            Cursor startC1 = mission_db.readData("missions", "start_point");
            Cursor destinationC1 = mission_db.readData("missions", "destination");
            if (nameC1.moveToFirst()) {
                for (int i = 0; i < entries; i++) {
                    nameArr[i] = nameC1.getString(0);
                    nameC1.moveToNext();
                }
            }
            if (startC1.moveToFirst()) {
                for (int i = 0; i < entries; i++) {
                    startArr[i] = startC1.getString(0);
                    startC1.moveToNext();
                }
            }
            if (destinationC1.moveToFirst()) {
                for (int i = 0; i < entries; i++) {
                    destArr[i] = destinationC1.getString(0);
                    destinationC1.moveToNext();
                }
            }
        }
        for (int i = 0; i < entries; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0));
            TextView missionname = new TextView(this);
            TextView startbody = new TextView(this);
            TextView stopbody = new TextView(this);

            missionname.setText(nameArr[i]);
            missionname.setTextSize(20);
            missionname.setTypeface(null, Typeface.BOLD);

            startbody.setText(startArr[i]);
            startbody.setTextSize(20);

            stopbody.setText(destArr[i]);
            stopbody.setTextSize(20);

            tableRow.addView(missionname);
            tableRow.addView(startbody);
            tableRow.addView(stopbody);
            dashlay.addView(tableRow);
            final String current = nameArr[i];
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MISSION_DISPLAY", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", current);
                    editor.apply();
                    Intent overview = new Intent(Dashboard.this, MissionOverviewActivity.class);
                    startActivity(overview);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(Dashboard.this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_ships) {

            Intent shipsView = new Intent(Dashboard.this, Ships.class);
            startActivity(shipsView);

        } else if  (id == R.id.nav_payloads) {
            Intent payload_list = new Intent(Dashboard.this, Payloads.class);
            startActivity(payload_list);
        } else if (id == R.id.nav_settings) {
            Intent settings = new Intent(Dashboard.this, SettingsActivity.class);
            startActivity(settings);

        } else if (id == R.id.nav_sign_out) {
            myDB_Editor = myDB.edit();
            myDB_Editor.clear();
            myDB_Editor.apply();
            Intent login = new Intent(Dashboard.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
