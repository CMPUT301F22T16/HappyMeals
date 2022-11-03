package com.example.happymeals;

import java.util.Date;

import java.util.HashMap;


public class Ingredient implements Storable{
    private String category;
    private String description;
    private Integer amount;
    private double cost;
    private Date date;
    private String location;
    private String id;

    public Ingredient(String category, String description, Integer amount, double cost, Date date, String location) {

        this.category = category;
        this.description = description;
        this.amount = amount;
        this.cost = cost;
        this.date = date;
        this.location = location;
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

    public double getCost() {
        return cost;
    }

    public Date getDate() {
        return date;
    }

    public String getLoc() {
        return location;
    }

    void setLoc(String loc) {
        this.location = loc;
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
        data.put("location", this.location);
        return data;
    }
}
