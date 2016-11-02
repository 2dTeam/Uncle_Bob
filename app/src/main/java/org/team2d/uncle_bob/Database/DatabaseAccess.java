package org.team2d.uncle_bob.Database;

/**
 * Created by nikolaev on 01.11.16.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import org.team2d.uncle_bob.Database.ORM.PizzaORM;
import java.util.ArrayList;
import java.util.HashMap;
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
    public HashMap<Integer, PizzaORM> getPizza() {
        HashMap<Integer, PizzaORM> pizzaMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM pizza", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Log.d(TAG, "test" + cursor.getColumnIndex("name"));
            String pizzaName = cursor.getString(cursor.getColumnIndex("name"));
            String pizzaImage = cursor.getString(cursor.getColumnIndex("pizza_image"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int onlineId = cursor.getInt(cursor.getColumnIndex("online_id"));
            PizzaORM pizza = new PizzaORM(id, onlineId, pizzaName, pizzaImage);
            pizzaMap.put(id, pizza);
            cursor.moveToNext();
        }
        cursor.close();
        return pizzaMap;
    }
}
