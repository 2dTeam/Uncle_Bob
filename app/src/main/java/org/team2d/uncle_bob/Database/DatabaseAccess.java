package org.team2d.uncle_bob.Database;

/**
 * Created by nikolaev on 01.11.16.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.team2d.uncle_bob.Database.ORM.PizzaORM;

import java.util.HashMap;
import java.util.List;

class DatabaseAccess {
    private static final String TAG = DatabaseAccess.class.getSimpleName();
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DataBaseHelper(context);
    }

    static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    HashMap<Integer, PizzaORM> getAllPizzaFromDb() {
        HashMap<Integer, PizzaORM> pizzaMap = new HashMap<>();
        Cursor cursor = database.rawQuery("SELECT * FROM pizza JOIN pizza_cost ON pizza.id = pizza_cost.id", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            if (pizzaMap.containsKey(id)) {
                Float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
                Float cost = cursor.getFloat(cursor.getColumnIndex("cost"));

                HashMap<String, String> weightCosts = new HashMap<>();
                weightCosts.put(weight.toString(), cost.toString());
                PizzaORM pizza = pizzaMap.get(id);

                List<HashMap<String, String>> objPizzaList =  pizza.getCostParams();
                objPizzaList.add(weightCosts);

            } else {
                String pizzaName = cursor.getString(cursor.getColumnIndex("name"));
                String pizzaImage = cursor.getString(cursor.getColumnIndex("pizza_image"));
                int onlineId = cursor.getInt(cursor.getColumnIndex("online_id"));
                Float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
                Float cost = cursor.getFloat(cursor.getColumnIndex("cost"));

                HashMap<String, String> weightCosts = new HashMap<>();
                weightCosts.put(weight.toString(), cost.toString());

                PizzaORM pizza = new PizzaORM(id, onlineId, pizzaName, pizzaImage, weightCosts);
                pizzaMap.put(id, pizza); // id - Pizza object
            }
            cursor.moveToNext();
        }
        cursor.close();
        return pizzaMap;
    }
}
