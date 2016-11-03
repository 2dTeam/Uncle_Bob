package org.team2d.uncle_bob.Database.ORM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nikolaev on 03.11.16.
 */

public class PizzaORM {
    private int id;
    private int onlineId;
    private String name;
    private String imagePath;
    private List<HashMap<String, String>> costParams = new ArrayList<>();

    public PizzaORM(int id, int onlineId, String name, String imagePath, HashMap<String, String> costs) {
        this.id = id;
        this.onlineId = onlineId;
        this.name = name;
        this.imagePath = imagePath;
        this.costParams.add(costs);
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
    public List<HashMap<String, String>> getCostParams() {
        return costParams;
    }
}
