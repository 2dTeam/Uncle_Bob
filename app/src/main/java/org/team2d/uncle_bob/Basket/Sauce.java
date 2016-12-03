package org.team2d.uncle_bob.Basket;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.team2d.uncle_bob.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Sauce {
    public static final int SAUCE_PRICE = 20;

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private static final Map<Integer, Sauce> ID_MAP = new HashMap<>(3);
    private static final List<Sauce> SAUCE_LIST = new ArrayList<>(3);

    private final int price;
    private final int id = ID_GENERATOR.getAndIncrement();
    private final String title;

    public static List<Sauce> getSauces() {
        return SAUCE_LIST;
    }

    private Sauce(int price, String title) {
        this.price = price;
        this.title = title;
        ID_MAP.put(id, this);
        SAUCE_LIST.add(this);
    }

    public int getPrice() {
        return price;
    }

    public String getPrice(Fragment thingToGetResources) {
        String priceString = thingToGetResources.getString(R.string.item_price_prefix) + String.valueOf(getPrice());
        if (Character.isDefined(thingToGetResources.getString(R.string.item_price_postfix_unicode).charAt(0)))
            priceString += thingToGetResources.getString(R.string.item_price_postfix_unicode);
        else
            priceString += thingToGetResources.getString(R.string.item_price_postfix);

        return priceString;
    }

    public int toInt() {
        return id;
    }

    @Nullable
    public Sauce fromInt(int id) {
        return ID_MAP.get(id);
    }

    public String getTitle() {
        return title;
    }

    public static final Sauce OIL_BASIL = new Sauce(SAUCE_PRICE, "Масло на базилике");
    public static final Sauce OIL_THYME_ONION = new Sauce(SAUCE_PRICE, "Масло на тимьяне и чесноке");
    public static final Sauce OIL_ONION_RED_PEPPER = new Sauce(SAUCE_PRICE, "Масло на чесноке и красном перце");
}
