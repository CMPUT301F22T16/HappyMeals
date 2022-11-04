package com.example.happymeals;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Date;

public class IngredientTest{

    Ingredient ingredient;

    public Ingredient mockIngredient() {
        return new Ingredient("mockCategory", "mockDescription", 5, 6.50, new Date(), "mockLoc");
    }

    /**
     * Sets a category and gets it.
     */
    @Test
    public void testCategory() {
        ingredient = mockIngredient();
        ingredient.setCategory("TestingCategory");
        assertEquals("TestingCategory", ingredient.getCategory());
    }

    /**
     * Sets a description and gets it.
     */
    @Test
    public void testDescription() {
        ingredient = mockIngredient();
        ingredient.setDescription("TestingDescription");
        assertEquals("TestingDescription", ingredient.getDescription());
    }

    /**
     * Sets a amount and gets it.
     */
    @Test
    public void testAmount() {
        ingredient = mockIngredient();
        ingredient.setAmount(3);
        assertEquals(true, ingredient.getAmount() == 3);
    }

    /**
     * Sets a cost and gets it.
     */
    @Test
    public void testCost() {
        ingredient = mockIngredient();
        ingredient.setCost(3.4);
        assertEquals(true, ingredient.getCost() == 3.4);
    }

    /**
     * Sets a date and gets it.
     */
    @Test
    public void testDate() {
        ingredient = mockIngredient();
        ingredient.setDate(2022, 11, 3);
        assertEquals(true, ingredient.getYear() == 2022);
        assertEquals(true, ingredient.getMonth() == 11);
        assertEquals(true, ingredient.getDay() == 3);
    }

    /**
     * Sets a location and gets it.
     */
    @Test
    public void testLocation() {
        ingredient = mockIngredient();
        ingredient.setLoc("TestingLocation");
        assertEquals("TestingLocation", ingredient.getLoc());
    }

}
