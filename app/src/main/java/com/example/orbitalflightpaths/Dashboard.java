package com.example.orbitalflightpaths;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences myDB;
    SharedPreferences.Editor myDB_Editor;
    TextView user_display, user_name_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_New_Mission);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        user_name_display = headerView.findViewById(R.id.user_display_name);

        if (isSetName()){
            SharedPreferences settings_pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String cust_username = settings_pref.getString("name_pref", "");
            user_name_display.setText(cust_username);
        } else {
            Toast.makeText(this, "Please set your name in the settings section.", Toast.LENGTH_LONG).show();
        }

        myDB = getSharedPreferences("ORBITAL_DATA", MODE_PRIVATE);
        String user = myDB.getString("username", "error");
        user_display = headerView.findViewById(R.id.user_display_email);
        user_display.setText(user);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isSetName(){
        SharedPreferences settings_pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String cust_username = settings_pref.getString("name_pref", null);
        return cust_username != null;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ships) {

        } else if (id == R.id.nav_fuel) {

        } else if (id == R.id.nav_payloads) {

        } else if (id == R.id.nav_map) {

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
