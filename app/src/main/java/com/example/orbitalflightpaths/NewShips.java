package com.example.orbitalflightpaths;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewShips extends AppCompatActivity {
    DBHelper db_work;
    Button create;
    EditText shipname, shipmass;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ships);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shipname = findViewById(R.id.ship_name);
        shipmass = findViewById(R.id.ship_mass);

        db_work = new DBHelper(this);

        create = findViewById(R.id.new_ship_btn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, mass;
                name = shipname.getText().toString().trim();
                mass = shipmass.getText().toString().trim();
                saveToDB(name, mass);
                Intent shipscreen = new Intent(NewShips.this, Ships.class);
                startActivity(shipscreen);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent shipscreen = new Intent(this, Ships.class);
        startActivity(shipscreen);
    }

    private void saveToDB(String name, String mass){
        try{
            db_work.writeData("ships", name, mass);
        }
        catch (Exception e)
        {
            Log.d("NewShip.saveToDB-->", e.toString());
        }
    }

}
