package com.example.orbitalflightpaths;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Timer timer, noLogIn;
    SharedPreferences myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!is_LoggedIn()) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    }
            }, 500);
        } else {
            noLogIn = new Timer();
            noLogIn.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        }
    }

    private boolean is_LoggedIn(){
        myDB = getSharedPreferences("ORBITAL_DATA", MODE_PRIVATE);
        String current_user = myDB.getString("username", "");
        return current_user != null;
    }

}
