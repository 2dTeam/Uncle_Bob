package org.team2d.uncle_bob.Basket;

import java.util.HashMap;

/**
 * Created by nikolaev on 03.11.16.
 */

public class Basket {
    public enum ProductType {
        PIZZA,
        DRINK,
        SALAT,
    }
    private static HashMap<ProductType, Object> basket = null;

    public static HashMap<ProductType, Object> getBasket() {
        if (basket == null) {
            basket = new HashMap<>();
        }
        return basket;
    }


}
