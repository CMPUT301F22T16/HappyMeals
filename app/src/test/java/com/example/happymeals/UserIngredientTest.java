package com.example.happymeals;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Date;

public class UserIngredientTest {

    UserIngredient userIngredient;

    public UserIngredient mockIngredient() {
        return new UserIngredient("mockCategory", "mockDescription", 5.3, 6.50, new Date(), "mockLoc", "L");
    }

    /**
     * Sets a category and gets it.
     */
    @Test
    public void testCategory() {
        userIngredient = mockIngredient();
        userIngredient.setCategory("TestingCategory");
        assertEquals("TestingCategory", userIngredient.getCategory());
    }

    /**
     * Sets a description and gets it.
     */
    @Test
    public void testDescription() {
        userIngredient = mockIngredient();
        userIngredient.setDescription("TestingDescription");
        assertEquals("TestingDescription", userIngredient.getDescription());
    }

    /**
     * Sets a amount and gets it.
     */
    @Test
    public void testAmount() {
        userIngredient = mockIngredient();
        userIngredient.setAmount(3.0);
        assertEquals(true, userIngredient.getAmount() == 3);
    }

    /**
     * Sets a cost and gets it.
     */
    @Test
    public void testCost() {
        userIngredient = mockIngredient();
        userIngredient.setCost(3.4);
        assertEquals(true, userIngredient.getCost() == 3.4);
    }

    /**
     * Sets a date and gets it.
     */
    @Test
    public void testDate() {
        userIngredient = mockIngredient();
        userIngredient.setDate(2022, 11, 3);
        assertEquals(true, userIngredient.getYear() == 2022);
        assertEquals(true, userIngredient.getMonth() == 11);
        assertEquals(true, userIngredient.getDay() == 3);
    }

    /**
     * Sets a location and gets it.
     */
    @Test
    public void testLocation() {
        userIngredient = mockIngredient();
        userIngredient.setLoc("TestingLocation");
        assertEquals("TestingLocation", userIngredient.getLoc());
    }

}
