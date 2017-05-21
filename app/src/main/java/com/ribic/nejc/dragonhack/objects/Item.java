package com.ribic.nejc.dragonhack.objects;

/**
 * Created by Nejc on 20. 05. 2017.
 */

public class Item {
    public String id;
    public String name;
    public String price;
    public String description;
    public String favoritable;

    public Item(String id, String name, String price, String description, String favoritable){
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.favoritable = favoritable;
    }
}
