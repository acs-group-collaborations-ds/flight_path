package com.example.orbitalflightpaths;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Ships extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ships);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton new_ship = findViewById(R.id.fabNewShip);
        new_ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_ship = new Intent(Ships.this, NewShips.class);
                startActivity(new_ship);
            }
        });
    }

}
