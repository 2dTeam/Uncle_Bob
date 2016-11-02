package org.team2d.uncle_bob.Database.ORM;

/**
 * Created by nikolaev on 03.11.16.
 */

public class PizzaORM {
    private int id;
    private int onlineId;
    private String name;
    private String imagePath;

    public PizzaORM(int id, int onlineId, String name, String imagePath) {
        this.id = id;
        this.onlineId = onlineId;
        this.name = name;
        this.imagePath = imagePath;
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
