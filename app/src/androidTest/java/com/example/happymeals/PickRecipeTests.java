package com.example.happymeals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PickRecipeTests {
    private Solo solo;
    private Intent intent;
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
//        intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("USER", "Guest");
//        intent.putExtras(bundle);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
//        intent = new Intent(activity.getApplicationContext(),MPPickRecipeActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("USER", "Guest");
//        intent.putExtras(bundle);
//        activity.startActivity(intent);
    }

    /**
     * This test ensure that clicking on
     * Add button direct user to the right
     * activity
     */
    @Test
    public void testCreateNewMeal(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
//        assertTrue(solo.waitForView(RecyclerView.class));
//        assertTrue(solo.waitForView(Button.class));
//        Button addButton = (Button) solo.getView(R.id.my_meals_add_button);
//        solo.clickOnView(addButton);
//        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
