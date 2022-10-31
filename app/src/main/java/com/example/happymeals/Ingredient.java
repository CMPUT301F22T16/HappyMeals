package com.example.happymeals;

import java.util.Date;
import java.util.UUID;

public class Ingredient {
    private Integer amount;
    private Integer cost;
    private Date date;
    private String loc;
    private String description;
    private String id;

    public Ingredient(Integer amount, Integer cost, Date date, String locRef) {
        this.amount = amount;
        this.cost = cost;
        this.date = date;
        this.loc = locRef;
        this.id = UUID.randomUUID().toString();
    }

    public Ingredient(Integer amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getCost() {
        return cost;
    }

    public Date getDate() {
        return date;
    }

    public String getLoc() {
        return loc;
    }

    public String getId() { return id; }
}
