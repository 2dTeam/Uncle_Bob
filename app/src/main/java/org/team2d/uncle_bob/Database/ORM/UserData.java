package org.team2d.uncle_bob.Database.ORM;

/**
 * Created by nikolaev on 14.12.16.
 */

public class UserData {
    private static UserData instance = new UserData();

    private String name;
    private String address;
    private Integer tel;

    public static UserData getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getTel() {
        return tel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

}
