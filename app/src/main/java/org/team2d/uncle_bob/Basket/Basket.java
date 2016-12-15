package org.team2d.uncle_bob.Basket;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ORM.Items.ItemParams;
import org.team2d.uncle_bob.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nikolaev on 03.11.16.
 */

// TODO: save order between app launches
public class Basket {
    private static final Basket INSTANCE = new Basket();

    private Set<BasketItem> items = new HashSet<>();
    private Callback onBasketEmpty = null;
    private Callback onBasketNotEmpty = null;

    public static Basket getInstance() {
        return INSTANCE;
    }

    public void addItem(BasketItem item) {
        final boolean wasEmpty = items.isEmpty();

        items.add(item);

        if (onBasketNotEmpty != null && wasEmpty)
            onBasketNotEmpty.call();
    }

    public void removeItem(BasketItem item) {
        items.remove(item);

        if (onBasketEmpty != null && items.isEmpty())
            onBasketEmpty.call();
    }

    public void emptyBasket() {
        items.clear();

        if (onBasketEmpty != null)
            onBasketEmpty.call();
    }

    @Nullable
    public BasketItem getItem(ItemObject item, ItemParams params) {
        for (BasketItem basketItem: items)
            if (basketItem.getItem() == item && basketItem.getDetails() == params)
                return basketItem;

        return null;
    }

    public Set<BasketItem> getItems() {
        return items;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (BasketItem item : items)
            totalPrice += item.getPrice();

        return totalPrice;
    }

    public String getTotalPrice(Fragment thingToGetResources) {
        String priceString = String.valueOf(getTotalPrice());
        if (Character.isDefined(thingToGetResources.getString(R.string.item_price_postfix_unicode).charAt(0)))
            priceString += thingToGetResources.getString(R.string.item_price_postfix_unicode);
        else
            priceString += thingToGetResources.getString(R.string.item_price_postfix);

        return priceString;
    }

    public void setOnBasketEmptyCallback(Callback callback) {
        onBasketEmpty = callback;
    }

    public void setOnBasketNotEmptyCallback(Callback callback) {
        onBasketNotEmpty = callback;
    }
    public interface Callback {
        void call();
    }
}
