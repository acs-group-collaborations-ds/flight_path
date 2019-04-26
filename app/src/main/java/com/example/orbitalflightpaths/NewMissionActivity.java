package com.example.orbitalflightpaths;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewMissionActivity extends AppCompatActivity {
    DBHelper populate;
    Spinner ships, payloads, start_point, destination;
    EditText mission_name;
    SharedPreferences mission_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mission);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populate = new DBHelper(this);
        ships = findViewById(R.id.ship_spinner);
        payloads = findViewById(R.id.payload_spinner);
        start_point = findViewById(R.id.start_point_spinner);
        destination = findViewById(R.id.destination_spinner);
        mission_name = findViewById(R.id.mission_name);

        ShipSpinner();
        PayloadSpinner();
        StartPointSpinner();
        DestinationSpinner();

        Button create = findViewById(R.id.create_mission);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FieldCheck()){
                    String mname = mission_name.getText().toString().trim();
                    String shipn = ships.getSelectedItem().toString().trim();
                    String payloadn = payloads.getSelectedItem().toString().trim();
                    String startn = start_point.getSelectedItem().toString().trim();
                    String destinationn = destination.getSelectedItem().toString().trim();
                    toMissionCalc(mname, shipn, payloadn, startn, destinationn);
                    Intent overview = new Intent(NewMissionActivity.this, MissionOverviewActivity.class);
                    startActivity(overview);
                    finish();
                } else {
                    Toast.makeText(NewMissionActivity.this, "Please enter valid values. Make sure the start point does not match the destination.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void toMissionCalc(String mission_name, String ship, String payload, String start_point, String destination){
//        mission_data = getSharedPreferences("MissionData", MODE_PRIVATE);
//        SharedPreferences.Editor editor = mission_data.edit();
//        editor.putString("name", mission_name);
//        editor.putString("ship", ship);
//        editor.putString("payload", payload);
//        editor.putString("start_point", start_point);
//        editor.putString("destination", destination);
//        editor.apply();

        String[] missionstuff = {mission_name, ship, payload, start_point, destination};
        Log.d("ArrayTest", "toMissionCalc: " + missionstuff[0] + " " + missionstuff[3]);
        populate.writeData("tempmission", missionstuff);
    }

    private boolean FieldCheck(){
        if (!mission_name.getText().toString().isEmpty()){
            return !start_point.getSelectedItem().toString().trim().equals(destination.getSelectedItem().toString().trim());
        }
        return false;
    }

    private void ShipSpinner(){
        List<String> spinnerArray = new ArrayList<String>();
        int shipno = (int)(long)populate.countTableItems("ships");
        if (shipno > 0) {
            Cursor nameC1 = populate.readData("ships", "shipname");
            if (nameC1.moveToFirst()) {
                for (int i = 0; i < shipno; i++) {
                    spinnerArray.add(nameC1.getString(0));
                    nameC1.moveToNext();
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ships.setAdapter(adapter);


    }

    private void PayloadSpinner(){
        List<String> spinnerArray = new ArrayList<String>();
        int payloadno = (int)(long)populate.countTableItems("payloads");
        if (payloadno > 0) {
            Cursor nameC1 = populate.readData("payloads", "payloadname");
            if (nameC1.moveToFirst()) {
                for (int i = 0; i < payloadno; i++) {
                    spinnerArray.add(nameC1.getString(0));
                    nameC1.moveToNext();
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        payloads.setAdapter(adapter);
    }


    private void StartPointSpinner(){
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Mercury");
        spinnerArray.add("Venus");
        spinnerArray.add("Earth");
        spinnerArray.add("Mars");
        spinnerArray.add("Jupiter");
        spinnerArray.add("Saturn");
        spinnerArray.add("Uranus");
        spinnerArray.add("Neptune");
        spinnerArray.add("Pluto");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        start_point.setAdapter(adapter);


    }

    private void DestinationSpinner(){
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Mercury");
        spinnerArray.add("Venus");
        spinnerArray.add("Earth");
        spinnerArray.add("Mars");
        spinnerArray.add("Jupiter");
        spinnerArray.add("Saturn");
        spinnerArray.add("Uranus");
        spinnerArray.add("Neptune");
        spinnerArray.add("Pluto");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        destination.setAdapter(adapter);
    }



}