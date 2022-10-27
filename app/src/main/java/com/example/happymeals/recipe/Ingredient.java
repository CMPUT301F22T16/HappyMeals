package com.example.happymeals.recipe;


public class Ingredient {
    private String desc;
    private String category;
    private boolean selected;

    public Ingredient(String desc) {
        this.desc = desc;
        this.category = null;
        this.selected = false;
    }

    public Ingredient(String desc, String category) {
        this.desc = desc;
        this.category = category;
        this.selected = false;
    }

    public String getDesc() {
        return desc;
    }

    public String getCategory() {
        return category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
