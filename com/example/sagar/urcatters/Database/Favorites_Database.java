package com.example.sagar.urcatters.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Favorites_Database extends SQLiteOpenHelper {
    private static final String DB = "Favorites_DB.db";
    public static final String Fav_Name = "name";
    private static final String Fav_Table = "fav_list";
    public static final String ID = "id";
    public static final String Shift_Name = "shiftName";
    private static final int version = 1;
    private SQLiteDatabase fav_myDB;

    public Favorites_Database(Context context) {
        super(context, DB, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE fav_list (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, shiftName TEXT NOT NULL  )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDB() {
        this.fav_myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (this.fav_myDB != null && this.fav_myDB.isOpen()) {
            this.fav_myDB.close();
        }
    }

    public long create(String name, String shift) {
        this.fav_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("shiftName", shift);
        return this.fav_myDB.insert(Fav_Table, null, values);
    }

    public long update(String name, String shift) {
        this.fav_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("shiftName", shift);
        return (long) this.fav_myDB.update(Fav_Table, values, "name = ?", new String[]{name});
    }

    public long delete(String name) {
        this.fav_myDB = getWritableDatabase();
        return (long) this.fav_myDB.delete(Fav_Table, "name = ?", new String[]{name});
    }

    public Cursor read() {
        this.fav_myDB = getWritableDatabase();
        return this.fav_myDB.rawQuery("SELECT * FROM fav_list", null);
    }
}
