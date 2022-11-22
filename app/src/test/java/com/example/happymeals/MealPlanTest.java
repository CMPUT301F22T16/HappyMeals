package com.example.happymeals;

import com.example.happymeals.meal.Meal;
import com.example.happymeals.mealplan.MealPlan;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class MealPlanTest {

    MealPlan mealplan;

    @Before
    public void mockMealPlan() {
        mealplan = new MealPlan();
    }

    @Test
    public void testGetDays() {
        Assert.assertEquals(mealplan.getNum_days(), 0);
    }

    @Test
    public void testGetBreakfast() {
        Assert.assertEquals(mealplan.getBreakfast().size(), 0);
    }

    @Test
    public void testGetLunch() {
        Assert.assertEquals(mealplan.getLunch().size(), 0);
    }

    @Test
    public void testGetDinner() {
        Assert.assertEquals(mealplan.getDinner().size(), 0);
    }

    @Test
    public void testSetNumDays() {
        mealplan.setDays(5);
        Assert.assertEquals(mealplan.getNum_days(), 5);
    }

    @Test
    public void testSetBreakfast() {
        Meal meal = new Meal();
        mealplan.setBreakfastWithIndex(meal, 0);

        Assert.assertEquals(mealplan.getBreakfast().size(), 1);
    }

    @Test
    public void testSetLunch() {
        Meal meal = new Meal();
        mealplan.setLunchWithIndex(meal, 0);

        Assert.assertEquals(mealplan.getLunch().size(), 1);
    }

    @Test
    public void testSetDinner() {
        Meal meal = new Meal();
        mealplan.setDinnerWithIndex(meal, 0);

        Assert.assertEquals(mealplan.getDinner().size(), 1);
    }

    @Test
    public void testGetStorable() {
        HashMap<String, Object> data = mealplan.getStorable();
        Assert.assertEquals(data.get("num_days"), 0);
        Assert.assertEquals(((List<Meal>)data.get("breakfast")).size(), 0);
        Assert.assertEquals(((List<Meal>)data.get("lunch")).size(), 0);
        Assert.assertEquals(((List<Meal>)data.get("dinner")).size(), 0);

    }

}
