package org.team2d.uncle_bob.Database.ORM;

import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;

import java.util.HashMap;

/**
 * Created by nikolaev on 06.11.16.
 */

public class ListOfItems {
    private HashMap<Integer, ItemObject> itemMap;

    ListOfItems() {
        itemMap = new HashMap<>();
    }

    public HashMap<Integer, ItemObject> getItemMap() {
        return itemMap;
    }

}
