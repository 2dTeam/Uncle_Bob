package org.team2d.uncle_bob.Database.ORM.Items;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikolaev on 06.11.16.
 */

public class ItemObject {
    private List<ItemParams> itemParams = null;
    private ItemParams cheapestItem = null;
    private int id;
    private int onlineId;
    private String name;
    private String imagePath;

    public ItemObject(int id, int onlineId, String name, String imagePath) {
        itemParams = new ArrayList<>();
        this.id = id;
        this.onlineId = onlineId;
        this.name = name;
        this.imagePath = imagePath;
    }

    public void addItemParams(ItemParams it) {
        itemParams.add(it);

        if (cheapestItem == null || cheapestItem.getCost() > it.getCost()) {
            cheapestItem = it;
        }
    }

    public List getAllItems() {
        return itemParams;
    }

    @Nullable
    public ItemParams getCheapestItem() {
        return cheapestItem;
    }

    public int getId() {
        return id;
    }

    public int getOnlineId() {
        return onlineId;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}
