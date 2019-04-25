package com.example.orbitalflightpaths;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Ships extends AppCompatActivity {
    DBHelper db_work;
    TableLayout shipRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ships);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db_work = new DBHelper(this);
        shipRow = findViewById(R.id.ships_table_layout);
        fromDB();

        FloatingActionButton new_ship = findViewById(R.id.fabNewShip);
        new_ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_ship = new Intent(Ships.this, NewShips.class);
                startActivity(new_ship);
                finish();
            }
        });
    }


    private void fromDB(){
        long ldbitems = db_work.countTableItems("ships");
        int dbitems = (int)(long) ldbitems;


        if (dbitems > 0) {
            String shipnamesArr[] = new String[dbitems];
            String shipmassArr[] = new String[dbitems];
            Log.d("Ships.java", "fromDB: dbitems val: " + dbitems);
            Cursor nameC1 = db_work.readData("ships", "shipname");
            Cursor massC1 = db_work.readData("ships", "shipmass");
            if (nameC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    shipnamesArr[i] = nameC1.getString(0);
                    nameC1.moveToNext();
                }
            }
            if (massC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    shipmassArr[i] = massC1.getString(0);
                    massC1.moveToNext();
                }
            }

            Log.d("Ships.java", "fromDB: string[0] vals: " + shipnamesArr[0] + " " + shipmassArr[0]);
            for (int i = 0; i < dbitems; i++) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0));
                TextView shipname = new TextView(this);
                TextView shipmass = new TextView(this);

                shipname.setText(shipnamesArr[i]);
                shipname.setTextSize(14);

                shipmass.setText(shipmassArr[i]);
                shipmass.setTextSize(14);

                tableRow.addView(shipname);
                tableRow.addView(shipmass);
                shipRow.addView(tableRow);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
