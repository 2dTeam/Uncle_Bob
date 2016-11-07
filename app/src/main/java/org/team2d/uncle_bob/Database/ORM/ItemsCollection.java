package org.team2d.uncle_bob.Database.ORM;

import org.team2d.uncle_bob.Database.ProductsEnum;

import java.util.HashMap;

/**
 * Created by nikolaev on 06.11.16.
 */

public class ItemsCollection {

    private static HashMap<ProductsEnum, ListOfItems> products = new HashMap<>();

    public static ListOfItems getListOfItem(ProductsEnum product) {
        if (!products.containsKey(product)) {
            ListOfItems list = new ListOfItems();
            products.put(product, list);
        }
        return products.get(product);
    }

}
