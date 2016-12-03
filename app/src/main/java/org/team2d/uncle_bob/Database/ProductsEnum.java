package org.team2d.uncle_bob.Database;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class ProductsEnum {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    private static final Map<Integer, ProductsEnum> ID_MAP = new HashMap<>();

    public static final ProductsEnum PIZZA = new ProductsEnum();
    public static final ProductsEnum DRINK = new ProductsEnum();
    public static final ProductsEnum SALAD = new ProductsEnum();
    public static final ProductsEnum REFRESHMENT = new ProductsEnum();

    private int id = COUNTER.getAndIncrement();

    @Nullable
    public static ProductsEnum fromInt(int id) {
        return ID_MAP.get(id);
    }
    public int toInt() {return id;}
    private ProductsEnum() {
        ID_MAP.put(id, this);
    }
}
