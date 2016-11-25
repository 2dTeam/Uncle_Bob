package org.team2d.uncle_bob.Database.ORM.Items;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by nikolaev on 07.11.16.
 */

public class ItemParams {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private static final Map<Integer, ItemParams> ID_MAP = new HashMap<>();

    private final float cost;
    private final float weight;
    private final int id = ID_GENERATOR.getAndIncrement();

    public ItemParams(Float cost, Float weight) {
        this.cost = cost;
        this.weight = weight;

        ID_MAP.put(id, this);
    }

    public float getCost() {
        return cost;
    }

    public float getWeight() {
        return weight;
    }

    public int toInt() {
        return id;
    }

    @Nullable
    public static ItemParams fromInt(int id) {
        return ID_MAP.get(id);
    }
}
