package com.example.happymeals;

import java.util.Date;
import java.util.HashMap;

public class Ingredient implements Storable{
    private String category;
    private String description;
    private Integer amount;
    private Integer cost;
    private Date date;
    private String loc;
    private String id;

    public Ingredient(String category, String description, Integer amount, Integer cost, Date date) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.cost = cost;
        this.date = date;
    }

    public String getCategory() { return  category; }

    public String getDescription() { return description; }

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

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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
