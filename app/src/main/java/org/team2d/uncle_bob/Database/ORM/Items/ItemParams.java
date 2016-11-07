package org.team2d.uncle_bob.Database.ORM.Items;

/**
 * Created by nikolaev on 07.11.16.
 */

public class ItemParams {
    private float cost;
    private float weight;

    public ItemParams(Float cost, Float weight) {
        this.cost = cost;
        this.weight = weight;
    }

    public float getCost() {
        return cost;
    }

    public float getWeight() {
        return weight;
    }
}
