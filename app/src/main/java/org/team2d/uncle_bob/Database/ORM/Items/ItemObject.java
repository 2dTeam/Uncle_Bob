package org.team2d.uncle_bob.Database.ORM.Items;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.team2d.uncle_bob.Database.ProductsEnum;
import org.team2d.uncle_bob.R;

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
    private String composition;
    @Nullable
    private ProductsEnum category;

    public ItemObject(int id, int onlineId, String name, String imagePath, @Nullable ProductsEnum category) {
        itemParams = new ArrayList<>();
        this.id = id;
        this.onlineId = onlineId;
        this.name = name;
        this.imagePath = imagePath;
        this.category = category;
    }

    public void addItemParams(ItemParams it) {
        itemParams.add(it);

        if (cheapestItem == null || cheapestItem.getCost() > it.getCost()) {
            cheapestItem = it;
        }
    }

    public List<ItemParams> getAllItems() {
        return itemParams;
    }

    @Nullable
    public ItemParams getCheapestItem() {
        return cheapestItem;
    }

    public String getLeastPrice(Fragment thingToGetResources) {
        String priceString = "";
        if (getCheapestItem() != null) {
            priceString = thingToGetResources.getString(R.string.item_price_prefix) + String.valueOf((int)Math.floor(getCheapestItem().getCost()));
            if (Character.isDefined(thingToGetResources.getString(R.string.item_price_postfix_unicode).charAt(0)))
                priceString += thingToGetResources.getString(R.string.item_price_postfix_unicode);
            else
                priceString += thingToGetResources.getString(R.string.item_price_postfix);
        }

        return priceString;
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

    @Nullable
    public ProductsEnum getCategory() {
        return category;
    }
}
