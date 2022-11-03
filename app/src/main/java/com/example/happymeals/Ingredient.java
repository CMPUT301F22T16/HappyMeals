package com.example.happymeals;

import com.example.happymeals.Storable;

import java.util.Date;

import java.util.HashMap;


public class Ingredient implements Storable {
    private String category;
    private String description;
    private Integer amount;
    private Double cost;
    private Date date;
    private String loc;
    private String id;

    public Ingredient(String category, String description, Integer amount, Double cost, Date date, String locRef) {

        this.category = category;
        this.description = description;
        this.amount = amount;
        this.cost = cost;
        this.date = date;
        this.loc = locRef;
        this.id = null;
    }

    public Ingredient(Integer amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public String getCategory() { return  category; }

    public String getDescription() { return description; }

    public Integer getAmount() {
        return amount;
    }

    public Double getCost() {
        return cost;
    }

    public Date getDate() {
        return date;
    }

    public String getLoc() {
        return loc;
    }

    void setLoc(String loc) {
        this.loc = loc;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    @Override
    public HashMap<String, Object> getStorable() {
        HashMap<String, Object> data = new HashMap();
        data.put("category", this.category);
        data.put("description", this.description);
        data.put("amount", this.amount);
        data.put("cost", this.cost);
        data.put("date", this.date);
        data.put("location", this.loc);
        return data;
    }
}
