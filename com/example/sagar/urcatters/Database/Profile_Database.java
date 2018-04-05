package com.example.sagar.urcatters.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Profile_Database extends SQLiteOpenHelper {
    public static final String Address = "address";
    public static final String City = "city";
    private static final String DB = "myDB.db";
    public static final String Email = "email";
    public static final String ID = "id";
    public static final String Mob = "mobile";
    public static final String Name = "name";
    public static final String PinCode = "pincode";
    private static final String Table_Name = "profile";
    private static final int version = 1;
    private SQLiteDatabase myDB;

    public Profile_Database(Context context) {
        super(context, DB, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE profile (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, mobile TEXT NOT NULL, address TEXT NOT NULL, city TEXT NOT NULL, pincode TEXT NOT NULL, email TEXT NOT NULL )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDB() {
        this.myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (this.myDB != null && this.myDB.isOpen()) {
            this.myDB.close();
        }
    }

    public long create(String name, String email, String mob, String address, String city, String pinCode) {
        this.myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put(Mob, mob);
        values.put(Address, address);
        values.put("email", email);
        values.put(City, city);
        values.put(PinCode, pinCode);
        return this.myDB.insert(Table_Name, null, values);
    }

    public long update(String name, String email, String mob, String address, String city, String pinCode) {
        this.myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put(Mob, mob);
        values.put(Address, address);
        values.put("email", email);
        values.put(City, city);
        values.put(PinCode, pinCode);
        return (long) this.myDB.update(Table_Name, values, "name = ?", new String[]{name});
    }

    public long delete(String name) {
        this.myDB = getWritableDatabase();
        return (long) this.myDB.delete(Table_Name, "name = ?", new String[]{name});
    }

    public Cursor read() {
        this.myDB = getWritableDatabase();
        return this.myDB.rawQuery("SELECT * FROM profile", null);
    }
}
