package org.team2d.uncle_bob.Database.ORM;

/**
 * Created by nikolaev on 14.12.16.
 */

public class UserData {
    private static UserData instance = new UserData();

    private String name;
    private String address;
    private String tel;

    public static UserData getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isEmpty(){
        if (this.getName().isEmpty() || this.getTel().isEmpty()
                || this.getAddress().isEmpty()){
            return false;
        }
        return true;
    }

}
