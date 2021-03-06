package com.example.orbitalflightpaths;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ofpLocal";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_SHIPS = "ships";
    private static final String TABLE_PAYLOADS = "payloads";
    private static final String TABLE_MISSIONS = "missions";
    private static final String TABLE_TEMP_MISSION = "tempmission";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_ships = "CREATE TABLE " + TABLE_SHIPS +"(shipname TEXT,shipmass TEXT)";
        String create_payloads = "CREATE TABLE " + TABLE_PAYLOADS +"(payloadname TEXT,payloadmass TEXT)";
        String create_missions = "CREATE TABLE " + TABLE_MISSIONS +"(name TEXT, start_point TEXT, destination TEXT, start_vel TEXT, insertion_burn TEXT, escape_vel_start TEXT, deltav_s TEXT, ending_vel TEXT, arrival_burn TEXT, actual_arrival_burn TEXT, escape_velocity_end TEXT, deltav_d TEXT, final_deltav TEXT)";
        String create_temp_missions = "CREATE TABLE " + TABLE_TEMP_MISSION + "(name TEXT, ship TEXT, payload TEXT, start TEXT, stop TEXT)";
        db.execSQL(create_ships);
        db.execSQL(create_payloads);
        db.execSQL(create_missions);
        db.execSQL(create_temp_missions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYLOADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MISSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMP_MISSION);
        onCreate(db);
    }

    public void writeData(String table, String param1,String param2){
        db=getWritableDatabase();
        switch (table){
            case TABLE_SHIPS:
                db.execSQL("insert into "+ table +" values ('"+ param1 +"','"+ param2 +"')");
                break;
            case TABLE_PAYLOADS:
                db.execSQL("insert into "+ table +" values ('"+ param1 +"','"+ param2 +"')");
                break;
            default:
                break;
        }
    }

    public void writeData(String table, String[] params){
        db = getWritableDatabase();
        switch (table){
            case TABLE_MISSIONS:
                String syntax1 = "insert into " + table + " values('" + params[0] + "', '" + params[1] + "', '" + params[2] + "', '"+ params[3] + "', '"+ params[4] + "', '"+ params[5] + "', '" + params[6] + "', '" + params[7] + "', '" + params[8] + "', '" + params[9] + "', '" + params[10] + "', '" + params[11] + "', '" + params[12] + "')";
                db.execSQL(syntax1);
                break;
            case TABLE_TEMP_MISSION:
                String part1 = "insert into " + table + " values('" + params[0] + "', '" + params[1] + "', '" + params[2] + "', '" + params[3] + "', '" + params[4] + "')";
                db.execSQL(part1);
                break;
            default:
                break;
        }
    }

    public Cursor existsindb(String table, String column, String val){
        Cursor c1 = db.rawQuery("select count("+ column +") from "+ table +" where "+ column +"='"+val+"'", null);
        return c1;
    }

//    public void updateData(String table, String param1,String param2){
//        db=getWritableDatabase();
//        switch (table){
//            case TABLE_SHIPS:
//                db.execSQL("update "+ table + " set name='"+ param1 +"'where regno='"+ param2 +"'");
//                break;
//            case TABLE_PAYLOADS:
//                db.execSQL("update "+ table + " set name='"+ param1 +"'where regno='"+ param2 +"'");
//                break;
//            default:
//                break;
//        }
//    }

    public void deleteData(String table){
        db=getWritableDatabase();
        db.execSQL("delete from " + table);
    }

    public Cursor readData(String table, String column){
        Cursor c1;
        db = this.getReadableDatabase();
        c1 = db.rawQuery("select " + column + " from " + table, null);
        return c1;
    }


    public Cursor readData(String table, String column, String data, String id) {
        Cursor c1 = null;
        try {
            db = this.getReadableDatabase();
            switch (table){
                case TABLE_SHIPS:
                    c1 = db.rawQuery("select "+ data +" from "+ table +" where "+ column +" = '"+id + "'", null);
                    break;
                case TABLE_PAYLOADS:
                    c1 = db.rawQuery("select "+ data +" from "+ table +" where "+ column +"= '"+id + "'", null);
                    break;
                case TABLE_MISSIONS:
                    c1 = db.rawQuery("select "+ data +" from "+ table +" where "+ column +"= '"+id + "'", null);
                    break;
                default:
                    break;
            }
        }
        catch (Exception e) {
            Log.e("DATABASE ERROR: ", e.toString());
        }
        return c1;
    }

    public long countTableItems(String table){
        db = this.getReadableDatabase();
        long countitems =  DatabaseUtils.queryNumEntries(db, table);
        db.close();

        return countitems;
    }

}

