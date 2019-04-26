package com.example.orbitalflightpaths;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MissionOverviewActivity extends AppCompatActivity {
    TextView start, stop, svel, iburn, esvel1, deltavs, envel, aburn, aaburn, esvel2, deltavd, finaldeltav, m_name;
    private String mname, shipn, payloadn, start_point, destination;
    FlightCalculator fCalc;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_overview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        start = findViewById(R.id.start_planet);
        stop = findViewById(R.id.dest_planet);
        svel = findViewById(R.id.start_vel);
        iburn = findViewById(R.id.insertion_burn);
        esvel1 = findViewById(R.id.escape_vel_s);
        deltavs = findViewById(R.id.deltav_s);
        envel = findViewById(R.id.end_vel);
        aburn = findViewById(R.id.arrival_burn);
        aaburn = findViewById(R.id.inf_d);
        esvel2 = findViewById(R.id.escape_vel_d);
        deltavd = findViewById(R.id.deltav_d);
        finaldeltav = findViewById(R.id.final_deltav);
        m_name = findViewById(R.id.mission_title_overview);

        fCalc = new FlightCalculator();
        db = new DBHelper(this);
        MissionFromDB();
        if (MissionExists(mname)){
            Toast.makeText(this, "A mission already exists with that name", Toast.LENGTH_SHORT).show();
        } else {

            MissionStart();
        }

    }

    private boolean MissionExists(String checkname){
        boolean checkresult = false;
        Cursor c1 = db.existsindb("missions", "name", checkname);
        if (c1.moveToFirst()){
            if (c1.getCount() > 0){
                checkresult = true;
            }
        }else {
            checkresult = false;
        }
        return checkresult;
    }

    private void MissionStart(){
        double start_vel , vel_inf_s, vel_inf_d, insertion_burn , escape_vel_start , deltav_s , ending_vel , arrival_burn , actual_arrival_burn , escape_velocity_end , deltav_d , final_deltav;
        double semimajoraxis;
        double mass, orbital_radius, orbital_velocity, planet_radius, uS, parking_orbit_radius_s, parking_orbit_circ_vel_s, vel_hyper_s;
        double mass2, orbital_radius2, orbital_velocity2, planet_radius2, uD, parking_orbit_radius_d, parking_orbit_circ_vel_d, vel_hyper_d;

        try {
            mass = Double.parseDouble(body_data(start_point, 0));
            orbital_radius = Double.parseDouble(body_data(start_point, 1));
            orbital_velocity = Double.parseDouble(body_data(start_point, 2));
            planet_radius = Double.parseDouble(body_data(start_point, 3));
            mass2 = Double.parseDouble(body_data(destination, 0));
            orbital_radius2 = Double.parseDouble(body_data(destination, 1));
            orbital_velocity2 = Double.parseDouble(body_data(destination, 2));
            planet_radius2 = Double.parseDouble(body_data(destination, 3));

            Log.d("BodyData", "MissionStart: " + mass);
            //STARTING BODY
            semimajoraxis = fCalc.SemiMajorAxis(orbital_radius, orbital_radius2);
            orbital_velocity = fCalc.OrbitVelocity(orbital_radius);
            start_vel = fCalc.Velocity(orbital_radius, semimajoraxis);
            vel_inf_s = fCalc.VelocityInf(start_vel, orbital_velocity);
            uS = fCalc.uS(mass);
            parking_orbit_radius_s = fCalc.ParkingOrbitRadius(planet_radius);
            parking_orbit_circ_vel_s = fCalc.ParkingOrbitCircularVel(uS, parking_orbit_radius_s);
            escape_vel_start = fCalc.VelocityEscape(uS, parking_orbit_radius_s);
            vel_hyper_s = fCalc.VelocityHyper(vel_inf_s, escape_vel_start);
            deltav_s = fCalc.DeltaV(vel_hyper_s, parking_orbit_circ_vel_s);

            //DESTINATION BODY
            orbital_velocity2 = fCalc.OrbitVelocity(orbital_radius2);
            ending_vel = fCalc.Velocity(orbital_radius2, semimajoraxis);
            vel_inf_d = fCalc.VelocityInf(ending_vel, orbital_velocity2);
            uD = fCalc.uS(mass2);
            parking_orbit_radius_d = fCalc.ParkingOrbitRadius(planet_radius2);
            parking_orbit_circ_vel_d = fCalc.ParkingOrbitCircularVel(uD, parking_orbit_radius_d);
            escape_velocity_end = fCalc.VelocityEscape(uD, parking_orbit_radius_d);
            vel_hyper_d = fCalc.VelocityHyper(vel_inf_d, escape_velocity_end);
            deltav_d = fCalc.DeltaV(vel_hyper_d, parking_orbit_circ_vel_d);


            //FINAL
            final_deltav = fCalc.FinalDeltaV(deltav_s, deltav_d);
            Log.d("DV", "MissionStart: deltavf" + final_deltav);
            String[] mission = {start_point, destination, String.valueOf(orbital_velocity), String.valueOf(start_vel),
                    String.valueOf(escape_vel_start), String.valueOf(deltav_s), String.valueOf(orbital_velocity2),
                    String.valueOf(ending_vel), String.valueOf(vel_inf_d), String.valueOf(escape_velocity_end),
                    String.valueOf(deltav_d), String.valueOf(final_deltav), null};

            //TO DB
            db.writeData("missions", mission);
            MissionDisplay(mission);
        }
        catch (Exception e){
            Log.e("MissionOverview-->", "MissionStart: fetching body data " + e.toString());
        }


    }


    private void MissionDisplay(String[] params){
        m_name.setText(mname);
        start.setText(params[0]);
        stop.setText(params[1]);
        svel.setText(params[2]);
        iburn.setText(params[3]);
        esvel1.setText(params[4]);
        deltavs.setText(params[5]);
        envel.setText(params[6]);
        aburn.setText(params[7]);
        aaburn.setText(params[8]);
        esvel2.setText(params[9]);
        deltavd.setText(params[10]);
        finaldeltav.setText(params[11]);
    }

    private void MissionFromDB(){
        long ldbitems = db.countTableItems("tempmission");
        int dbitems = (int)(long) ldbitems;
        Log.d("dbitems", "MissionFromDB: " + dbitems);

        if (dbitems > 0) {
            String mnameArr[] = new String[dbitems];
            String shipnArr[] = new String[dbitems];
            String payloadnArr[] = new String[dbitems];
            String start_pointArr[] = new String[dbitems];
            String destinationArr[] = new String[dbitems];
            Log.d("Payloads.java", "MissionFromDB: dbitems val: " + dbitems);
            Cursor nameC1 = db.readData("tempmission", "name");
            Cursor shipC1 = db.readData("tempmission", "ship");
            Cursor payloadC1 = db.readData("tempmission", "payload");
            Cursor startC1 = db.readData("tempmission", "start");
            Cursor stopC1 = db.readData("tempmission", "stop");
            if (nameC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    mnameArr[i] = nameC1.getString(0);
                    nameC1.moveToNext();
                }
            }
            if (shipC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    shipnArr[i] = shipC1.getString(0);
                    shipC1.moveToNext();
                }
            }
            if (payloadC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    payloadnArr[i] = payloadC1.getString(0);
                    payloadC1.moveToNext();
                }
            }
            if (startC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    start_pointArr[i] = startC1.getString(0);
                    startC1.moveToNext();
                }
            }
            if (stopC1.moveToFirst()) {
                for (int i = 0; i < dbitems; i++) {
                    destinationArr[i] = stopC1.getString(0);
                    stopC1.moveToNext();
                }
            }

            mname = mnameArr[0];
            shipn = shipnArr[0];
            payloadn = payloadnArr[0];
            start_point = start_pointArr[0];
            destination = destinationArr[0];
            Log.d("Vars", "MissionFromDB: Arr contains: " + mnameArr[0] + " " + start_pointArr[0]);
            db.deleteData("tempmission");
        }
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

        if (body_name.contains("Mercury")){
            return Mercury[index];
        } else if (body_name.contains("Venus")){
            return Venus[index];
        } else if (body_name.contains("Earth")){
            return Earth[index];
        } else if (body_name.contains("Mars")){
            return Mars[index];
        } else if (body_name.contains("Jupiter")){
            return Jupiter[index];
        } else if (body_name.contains("Saturn")){
            return Saturn[index];
        } else if (body_name.contains("Uranus")){
            return Uranus[index];
        } else if (body_name.contains("Neptune")){
            return Neptune[index];
        } else if (body_name.contains("Pluto")){
            return Pluto[index];
        } else {
            return "error";
        }
    }

    @Override
    public void onBackPressed() {
        Intent dash = new Intent(MissionOverviewActivity.this, Dashboard.class);
        startActivity(dash);
        finish();
    }


}
