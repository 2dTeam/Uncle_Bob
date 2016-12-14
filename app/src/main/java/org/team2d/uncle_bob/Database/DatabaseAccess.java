package org.team2d.uncle_bob.Database;

/**
 * Created by nikolaev on 01.11.16.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ORM.Items.ItemParams;
import org.team2d.uncle_bob.Database.ORM.ItemsCollection;
import org.team2d.uncle_bob.Database.ORM.UserData;

import java.util.HashMap;

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

    void close() {
        if (database != null) {
            this.database.close();
        }
    }

    void loadUserData() {

        UserData user = UserData.getInstance();

        String query = "SELECT * FROM user";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String tel = cursor.getString(cursor.getColumnIndex("tel"));

            user.setAddress(address);
            user.setName(name);
            user.setTel(tel);
            cursor.moveToNext();
        }
        cursor.close();
    }

    void setUserData(String name, String address, String tel) {
        String query = String.format("UPDATE user SET name='%s', address='%s', tel='%s' WHERE id=1",  name, address, tel);
        Cursor cursor = database.rawQuery(query, null);
        cursor.close();

        UserData user = UserData.getInstance();
        user.setAddress(address);
        user.setName(name);
        user.setTel(tel);
    }

    void loadPizzaFromDb() {
        HashMap <Integer, ItemObject> pizzaMap = ItemsCollection.getListOfItem(ProductsEnum.PIZZA).getItemMap();
        String query = "SELECT * FROM pizza JOIN pizza_cost ON pizza.id = pizza_cost.id";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            //TODO I think there is a better way to handle weight/cost params (make 2 queries: for pizza and its costs)
            if (pizzaMap.containsKey(id)) {
                Float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
                Float cost = cursor.getFloat(cursor.getColumnIndex("cost"));
                pizzaMap.get(id).addItemParams(new ItemParams(cost, weight));
            } else {
                String pizzaName = cursor.getString(cursor.getColumnIndex("name"));
                String pizzaImage = cursor.getString(cursor.getColumnIndex("pizza_image"));
                int onlineId = cursor.getInt(cursor.getColumnIndex("online_id"));
                Float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
                Float cost = cursor.getFloat(cursor.getColumnIndex("cost"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                ItemObject pizza = new ItemObject(id, onlineId, pizzaName,
                        pizzaImage, description, ProductsEnum.PIZZA); // I hardcoded PIZZA here because we query 'pizza' DB.

                pizza.addItemParams(new ItemParams(cost, weight));
                pizzaMap.put(id, pizza); // id - Pizza object
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    void loadDrinksFromDb() {
        HashMap <Integer, ItemObject> drinksMap = ItemsCollection.getListOfItem(ProductsEnum.DRINK).getItemMap();
        String query = "SELECT * FROM drinks";
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            int onlineId = cursor.getInt(cursor.getColumnIndex("online_id"));
            Float cost = cursor.getFloat(cursor.getColumnIndex("cost"));
            Float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            ItemObject drink = new ItemObject(id, onlineId, name,
                    image, description, ProductsEnum.DRINK);

            drink.addItemParams(new ItemParams(cost, weight));
            drinksMap.put(id, drink); // id - Pizza object
            cursor.moveToNext();
        }
        cursor.close();
    }

    void loadRefreshmentsFromDb() {
        HashMap <Integer, ItemObject> refreshmentMap = ItemsCollection.getListOfItem(ProductsEnum.REFRESHMENT).getItemMap();
        String query = "SELECT * FROM refreshment";
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            int onlineId = cursor.getInt(cursor.getColumnIndex("online_id"));
            Float cost = cursor.getFloat(cursor.getColumnIndex("cost"));
            Float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            ItemObject refreshment = new ItemObject(id, onlineId, name,
                    image, description, ProductsEnum.REFRESHMENT);

            refreshment.addItemParams(new ItemParams(cost, weight));
            refreshmentMap.put(id, refreshment); // id - Pizza object
            cursor.moveToNext();
        }

        cursor.close();
    }
}
