package com.example.orbitalflightpaths;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ofpLocal";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SHIPS = "ships";
    private static final String TABLE_PAYLOADS = "payloads";
    private static final String TABLE_MISSIONS = "missions";
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_ships = "CREATE TABLE " + TABLE_SHIPS +"(shipname TEXT,shipmass TEXT)";
        String create_payloads = "CREATE TABLE " + TABLE_PAYLOADS +"(payloadname TEXT,payloadmass TEXT)";
        String create_missions = "CREATE TABLE " + TABLE_MISSIONS +"(regno TEXT,name TEXT)";
        db.execSQL(create_ships);
        db.execSQL(create_payloads);
        db.execSQL(create_missions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYLOADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MISSIONS);
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

    public void updateData(String table, String param1,String param2){
        db=getWritableDatabase();
        switch (table){
            case TABLE_SHIPS:
                db.execSQL("update "+ table + " set name='"+ param1 +"'where regno='"+ param2 +"'");
                break;
            case TABLE_PAYLOADS:
                db.execSQL("update "+ table + " set name='"+ param1 +"'where regno='"+ param2 +"'");
                break;
            default:
                break;
        }
    }

    public void deleteData(String regno){
        db=getWritableDatabase();
        db.execSQL("delete from mytable where regno='"+regno+"'");
    }

    public Cursor readData(String table, String column, String data, String id) {
        Cursor c1 = null;
        try {
            db = this.getReadableDatabase();
            switch (table){
                case TABLE_SHIPS:
                    c1 = db.rawQuery("select "+ data +" from "+ table +" where "+ column +"="+id, null);
                    break;
                case TABLE_PAYLOADS:
                    c1 = db.rawQuery("select "+ data +" from "+ table +" where "+ column +"="+id, null);
                    break;
                case TABLE_MISSIONS:
                    c1 = db.rawQuery("select "+ data +" from "+ table +" where "+ column +"="+id, null);
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
}

