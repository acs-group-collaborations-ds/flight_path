package com.example.orbitalflightpaths;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Payloads extends AppCompatActivity {
    DBHelper db_work;
    TableLayout payloadrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payloads);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db_work = new DBHelper(this);
        payloadrow = findViewById(R.id.payloads_table_layout);
        fromDB();

        FloatingActionButton fab = findViewById(R.id.new_payload_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_payload = new Intent(Payloads.this, NewPayloadActivity.class);
                startActivity(new_payload);
                finish();
            }
        });
    }

    private void fromDB(){
        long ldbitems = db_work.countTableItems("payloads");
        int dbitems = (int)(long) ldbitems;


        if (dbitems > 0) {
            String payloadnamesArr[] = new String[dbitems];
            String payloadmassArr[] = new String[dbitems];
            Log.d("Payloads.java", "fromDB: dbitems val: " + dbitems);
            Cursor nameC1 = db_work.readData("payloads", "payloadname");
            Cursor massC1 = db_work.readData("payloads", "payloadmass");
            if (nameC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    payloadnamesArr[i] = nameC1.getString(0);
                    nameC1.moveToNext();
                }
            }
            if (massC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    payloadmassArr[i] = massC1.getString(0);
                    massC1.moveToNext();
                }
            }

            Log.d("Ships.java", "fromDB: string[0] vals: " + payloadnamesArr[0] + " " + payloadmassArr[0]);
            for (int i = 0; i < dbitems; i++) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0));
                TextView payloadname = new TextView(this);
                TextView payloadmass = new TextView(this);

                payloadname.setText(payloadnamesArr[i]);
                payloadname.setTextSize(14);

                payloadmass.setText(payloadmassArr[i]);
                payloadmass.setTextSize(14);

                tableRow.addView(payloadname);
                tableRow.addView(payloadmass);
                payloadrow.addView(tableRow);
            }
        }
    }

}
