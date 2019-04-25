package com.example.orbitalflightpaths;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class NewMissionActivity extends AppCompatActivity {
    FlightCalculator path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mission);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        path = new FlightCalculator();

        Button create = findViewById(R.id.create_mission);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent overview = new Intent(NewMissionActivity.this, MissionOverviewActivity.class);
                startActivity(overview);
                finish();
            }
        });
    }


    private String body_data(String body_name, int index){
        String Mercury[] = {"3.3e+23", "57895200000", "47.4", "2439.5"};
        String Venus[] = {"4.87e+24", "108160800000", "35.0", "6052"};
        String Earth[] = {"5.97e+24", "149600000000", "29.8", "6378"};
        String Mars[] = {"6.42e+23", "227990400000", "24.1", "3396"};
        String Jupiter[] = {"1.898e+27", "778368800000", "13.1", "71492"};
        String Saturn[] = {"5.68e+26", "1.4267352e+12", "9.7", "60268"};
        String Uranus[] = {"8.68e+25", "2.28709736e+12", "6.8", "25559"};
        String Neptune[] = {"1.02e+26", "4.4983224e+12", "5.4", "24764"};
        String Pluto[] = {"1.46e+22", "5.9065072e+12", "4.7", "1185"};

        if (body_name.contains("mercury")){
            return Mercury[index];
        } else if (body_name.contains("venus")){
            return Venus[index];
        } else if (body_name.contains("earth")){
            return Earth[index];
        } else if (body_name.contains("mars")){
            return Mars[index];
        } else if (body_name.contains("jupiter")){
            return Jupiter[index];
        } else if (body_name.contains("saturn")){
            return Saturn[index];
        } else if (body_name.contains("uranus")){
            return Uranus[index];
        } else if (body_name.contains("neptune")){
            return Neptune[index];
        } else if (body_name.contains("pluto")){
            return Pluto[index];
        } else {
            return null;
        }
    }

}