package org.team2d.uncle_bob.Database;

import android.content.Context;

import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ORM.ItemsCollection;
import org.team2d.uncle_bob.Database.ORM.MapOfItems;

import java.util.Comparator;
import java.util.List;

/**
 * Created by nikolaev on 03.11.16.
 */
public class DatabaseService {
    private static List<ItemObject> pizza = null;
    private static List<ItemObject> drinks = null;
    private static List<ItemObject> refreshments = null;
    private static boolean  mLoaded = false;

    public static synchronized void loadDB(Context context) {
        if (!mLoaded) {
            loadObjectsFromDB(context);
            mLoaded = true;
        }
    }
    private static synchronized void loadObjectsFromDB(Context context){
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();

        databaseAccess.loadPizzaFromDb();
        databaseAccess.loadDrinksFromDb();
        databaseAccess.loadRefreshmentsFromDb();

        getPizzaSortedByCost();
        getDrinksSortedByCost();
        getRefreshmentsSortedByCost();

        databaseAccess.close();
    }



    public static List <ItemObject> getPizzaSortedByCost () {
        if (pizza == null) {
            MapOfItems pizzaObj = ItemsCollection.getListOfItem(ProductsEnum.PIZZA);

            pizza = pizzaObj.getSortedListOfItems(new Comparator<ItemObject>() {
                public int compare(ItemObject o1, ItemObject o2) {
                    return o1.getCheapestItem().getCost() < o2.getCheapestItem().getCost() ? -1
                            : o1.getCheapestItem().getCost() > o2.getCheapestItem().getCost() ? 1
                            : 0;
                }
            });
        }
        return pizza;
    }

    public static List <ItemObject> getDrinksSortedByCost () {
        if (drinks == null) {
            MapOfItems drinksObj = ItemsCollection.getListOfItem(ProductsEnum.DRINK);

            drinks = drinksObj.getSortedListOfItems(new Comparator<ItemObject>() {
                public int compare(ItemObject o1, ItemObject o2) {
                    return o1.getCheapestItem().getCost() < o2.getCheapestItem().getCost() ? -1
                            : o1.getCheapestItem().getCost() > o2.getCheapestItem().getCost() ? 1
                            : 0;
                 }
            });
        }
        return drinks;
    }

    public static List <ItemObject> getRefreshmentsSortedByCost () {
        if (refreshments == null) {
            MapOfItems refreshmentsObj = ItemsCollection.getListOfItem(ProductsEnum.REFRESHMENT);

            refreshments = refreshmentsObj.getSortedListOfItems(new Comparator<ItemObject>() {
                public int compare(ItemObject o1, ItemObject o2) {
                    return o1.getCheapestItem().getCost() < o2.getCheapestItem().getCost() ? -1
                            : o1.getCheapestItem().getCost() > o2.getCheapestItem().getCost() ? 1
                            : 0;
                }
            });
        }
        return refreshments;
    }

}
