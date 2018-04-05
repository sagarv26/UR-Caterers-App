package com.example.sagar.urcatters.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Breakfast_Database extends SQLiteOpenHelper {
    public static final String BF_Name = "name";
    private static SQLiteDatabase BF_myDB = null;
    private static final String Break_Fast = "bf_list";
    private static final String DB = "Breakfast_DB.db";
    public static final String ID = "id";
    public static final String Shift_Name = "shiftName";
    private static final int version = 1;

    public Breakfast_Database(Context context) {
        super(context, DB, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bf_list (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, shiftName TEXT NOT NULL  )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDB() {
        BF_myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (BF_myDB != null && BF_myDB.isOpen()) {
            BF_myDB.close();
        }
    }

    public long create(String name, String shift) {
        BF_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("shiftName", shift);
        return BF_myDB.insert(Break_Fast, null, values);
    }

    public long update(String name, String shift) {
        BF_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("shiftName", shift);
        return (long) BF_myDB.update(Break_Fast, values, "name = ?", new String[]{name});
    }

    public long delete(String name) {
        BF_myDB = getWritableDatabase();
        return (long) BF_myDB.delete(Break_Fast, "name = ?", new String[]{name});
    }

    public Cursor read() {
        BF_myDB = getWritableDatabase();
        return BF_myDB.rawQuery("SELECT * FROM bf_list", null);
    }

    public int getCount(String shift) {
        int getCount = 0;
        try {
            BF_myDB = getWritableDatabase();
            Cursor res = BF_myDB.rawQuery("SELECT * FROM bf_list", null);
            while (res.moveToNext()) {
                String name = res.getString(res.getColumnIndex("name"));
                String shiftName = res.getString(res.getColumnIndex("shiftName"));
                Object obj = -1;
                switch (shift.hashCode()) {
                    case 65071099:
                        if (shift.equals("Chats")) {
                            obj = 3;
                            break;
                        }
                        break;
                    case 71930176:
                        if (shift.equals("Juice")) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 73782026:
                        if (shift.equals("Lunch")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 106543547:
                        if (shift.equals("Breakfast")) {
                            obj = null;
                            break;
                        }
                        break;
                    case 2047137938:
                        if (shift.equals("Dinner")) {
                            obj = 2;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                        if (!shiftName.contains("Breakfast")) {
                            break;
                        }
                        getCount++;
                        break;
                    case 1:
                        if (!shiftName.contains("Lunch")) {
                            break;
                        }
                        getCount++;
                        break;
                    case 2:
                        if (!shiftName.contains("Dinner")) {
                            break;
                        }
                        getCount++;
                        break;
                    case 3:
                        if (!shiftName.contains("Chats")) {
                            break;
                        }
                        getCount++;
                        break;
                    case 4:
                        if (!shiftName.contains("Juice")) {
                            break;
                        }
                        getCount++;
                        break;
                    default:
                        break;
                }
            }
            return getCount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int validate(String name, String shift) {
        try {
            Cursor c = BF_myDB.rawQuery("SELECT * FROM bf_list WHERE name=? AND shiftName=?", new String[]{name, shift});
            c.moveToFirst();
            int i = c.getCount();
            c.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
