package org.team2d.uncle_bob.Database.ORM;

import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nikolaev on 06.11.16.
 */

public class MapOfItems {
    private HashMap<Integer, ItemObject> itemMap;

    MapOfItems() {
        itemMap = new HashMap<>();
    }

    public HashMap<Integer, ItemObject> getItemMap() {
        return itemMap;
    }

    public List<ItemObject> getSortedListOfItems(Comparator<ItemObject> cmp) {
        List<ItemObject> items = new ArrayList<>(itemMap.values());
        Collections.sort(items, cmp);
        return items;
    }
}
