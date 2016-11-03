package org.team2d.uncle_bob.Database;

import android.content.Context;

import org.team2d.uncle_bob.Database.ORM.PizzaORM;

import java.util.HashMap;

/**
 * Created by nikolaev on 03.11.16.
 */

public class DatabaseService {
    private static HashMap<Integer, PizzaORM> pizza = null;

    public  static HashMap<Integer, PizzaORM> getPizza(Context context) {
        if (pizza == null) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
            databaseAccess.open();
            pizza = databaseAccess.getAllPizzaFromDb();
        }

        return pizza;
    }

}
