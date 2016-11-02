package org.team2d.uncle_bob.Database;

/**
 * Created by nikolaev on 01.11.16.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private static final String TAG = DatabaseAccess.class.getSimpleName();
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;


    private DatabaseAccess(Context context) {
        this.openHelper = new DataBaseHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getPizza() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM pizza", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.d(TAG, "data" + cursor.getString(cursor.getColumnIndex("Name")));
            list.add(cursor.getString(cursor.getColumnIndex("Name")));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
