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

public class NewPayloadActivity extends AppCompatActivity {
    DBHelper db_work;
    Button create;
    EditText payloadname, payloadmass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payload);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        payloadname = findViewById(R.id.payload_name);
        payloadmass = findViewById(R.id.payload_mass);

        db_work = new DBHelper(this);

        create = findViewById(R.id.add_new_payload_btn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, mass;
                name = payloadname.getText().toString().trim();
                mass = payloadmass.getText().toString().trim();
                saveToDB(name, mass);
                Intent shipscreen = new Intent(NewPayloadActivity.this, Payloads.class);
                startActivity(shipscreen);
                finish();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent payloadscreen = new Intent(NewPayloadActivity.this, Payloads.class);
        startActivity(payloadscreen);
    }

    private void saveToDB(String name, String mass){
        try{
            db_work.writeData("payloads", name, mass);
        }
        catch (Exception e)
        {
            Log.d("NewShip.saveToDB-->", e.toString());
        }
    }

}
