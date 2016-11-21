package org.team2d.uncle_bob.Basket;

import org.team2d.uncle_bob.Database.ORM.Items.ItemObject;
import org.team2d.uncle_bob.Database.ORM.Items.ItemParams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasketItem {
    private ItemObject item;
    private ItemParams details;
    private int quantity = 1;
    private Set<Sauce> sauces = new HashSet<>(3); // Should be a set.

    public BasketItem(ItemObject item, ItemParams details) {
        this.item = item;
        this.details = details;
    }

    public ItemObject getItem() {
        return item;
    }

    public ItemParams getDetails() {
        return details;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addSauce(Sauce sauce) {
        if (!sauces.contains(sauce))
            sauces.add(sauce);
    }

    public void removeSauce(Sauce sauce) {
        sauces.remove(sauce);
    }

    public int getPrice() {
        int saucesPrice = 0;
        for (Sauce sauce : sauces)
            saucesPrice += sauce.getPrice();

        return (saucesPrice + (int) Math.ceil(details.getCost())) * quantity;
    }
}
