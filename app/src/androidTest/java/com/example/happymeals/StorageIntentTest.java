package com.example.happymeals;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.recipe.RecipeListActivity;
import com.example.happymeals.storage.Storage;
import com.example.happymeals.storage.StorageActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class StorageIntentTest {

    Solo solo;
    Storage storage;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    public void addMockStorages() {
        String name = "Fridge";
        storage = new Storage(name);
        // Make sure the there are atleast 2 things in adapter other wise add ramen and sushi
        ListView recipe_list_view = (ListView) solo.getView(R.id.storage_grid);
        ListAdapter recipe_list_adapter = recipe_list_view.getAdapter();

        if (recipe_list_adapter.getCount() > 0) {
            return;
        }

        // Add a new storage
        solo.clickOnButton("Storage");
        solo.sleep(500);

        EditText textName = (EditText) solo.getView(R.id.storage_name_fragment);
        textName.setText(name);

        solo.clickOnText("Ok");

        solo.sleep(1000);

        solo.waitForText(name);

    }

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnView(solo.getView(R.id.storage_button));
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", StorageActivity.class);
        addMockStorages();
    }

    @Test
    public void testDisplayIngredient() {
        solo.clickOnText(storage.getStoreName());

        solo.sleep(1000);

        // Check if current activity changes
        solo.assertCurrentActivity("Wrong activity", IngredientActivity.class);

        // Get the adapter
        ListView ingredient_list_view = (ListView) solo.getView(R.id.storage_ingredient_list);
        ListAdapter list_adapter = ingredient_list_view.getAdapter();

        UserIngredient ingredient = (UserIngredient) list_adapter.getItem(0);

        Assert.assertEquals(ingredient.getLoc(), storage.getStoreName());

    }



    /**
     * Runs after all tests
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
