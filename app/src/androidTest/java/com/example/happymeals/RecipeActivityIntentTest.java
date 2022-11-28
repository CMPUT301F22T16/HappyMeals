package com.example.happymeals;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.recipe.EditRecipe;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeListActivity;
import com.example.happymeals.recipe.ViewRecipeActivity;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RecipeActivityIntentTest {

    private Solo solo;
    private FirebaseFirestore conn;
    private Recipe recipe1;
    private Recipe recipe2;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    public void addMockRecipes() {

        // Make sure the there are atleast 2 things in adapter other wise add ramen and sushi
        ListView recipe_list_view = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter = recipe_list_view.getAdapter();
        if (recipe_list_adapter.getCount() >= 2) {
            return;
        }

        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);


        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Make Ramen
        solo.enterText((EditText) solo.getView(R.id.recipe_title_edit_text), "Ramen");
        solo.enterText((EditText) solo.getView(R.id.recipe_prep_time_edit_text), "20");
        solo.enterText((EditText) solo.getView(R.id.recipe_num_serv_edit_text), "20");
        solo.enterText((EditText) solo.getView(R.id.recipe_category_edit_text), "Soup");
        solo.clickOnView(solo.getView(R.id.recipe_submit_button));
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Make Sushi
        solo.enterText((EditText) solo.getView(R.id.recipe_title_edit_text), "Sushi");
        solo.enterText((EditText) solo.getView(R.id.recipe_prep_time_edit_text), "10");
        solo.enterText((EditText) solo.getView(R.id.recipe_num_serv_edit_text), "1");
        solo.enterText((EditText) solo.getView(R.id.recipe_category_edit_text), "Snack");
        solo.clickOnView(solo.getView(R.id.recipe_submit_button));
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);

    }

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnView(solo.getView(R.id.recipe_button));
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        addMockRecipes();
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Tests the sorting functionality of the recipes (Sort by Title)
     */
    @Test
    public void testSortByTitleRecipes() throws InterruptedException {

        // Launch sort fragment
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_recipes));

        // Wait for fragment to pop up
        solo.sleep(1000);

        // Click OK to test order by Low to High and sort by Title sort
        solo.clickOnText("Ok");
        solo.sleep(1000);

        // Check if top recipe is Ramen
        ListView recipe_list_view = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter = recipe_list_view.getAdapter();

        // Get all recipes displayed
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            recipes.add((Recipe) recipe_list_adapter.getItem(i));
        }

        // Sort the list by title
        Collections.sort(recipes, (o1, o2) -> (o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase())));

        // Check if top items are same
        Recipe recipe = (Recipe) recipe_list_adapter.getItem(0);

        Assert.assertEquals(recipe.getTitle(), recipes.get(0).getTitle());


        // Launch sort fragment
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_recipes));

        // Wait for fragment to pop up
        solo.sleep(1000);

        // Click order by High to Low and sort by Title
        solo.clickOnButton("High to Low");
        solo.sleep(1000);
        solo.clickOnText("Ok");
        solo.sleep(1000);


        // Get all recipes displayed
        recipes = new ArrayList<>();
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            recipes.add((Recipe) recipe_list_adapter.getItem(i));
        }

        // Sort the list by title
        Collections.sort(recipes, (o1, o2) -> (o2.getTitle().toLowerCase().compareTo(o1.getTitle().toLowerCase())));

        // Check if top items are same
        recipe = (Recipe) recipe_list_adapter.getItem(0);

        Assert.assertEquals(recipe.getTitle(), recipes.get(0).getTitle());
    }

    /**
     * Tests the sorting functionality of the recipes (Sort by Preparation time)
     */
    @Test
    public void testSortByPrepTimeRecipes() throws InterruptedException {

        // Launch sort fragment
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_recipes));

        // Wait for fragment to pop up
        solo.sleep(1000);

        // Click OK to test order by Low to High and sort by prep time sort
        solo.clickOnText("Ok");
        solo.sleep(1000);

        // Check if top recipe is Ramen
        ListView recipe_list_view = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter = recipe_list_view.getAdapter();

        // Get all recipes displayed
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            recipes.add((Recipe) recipe_list_adapter.getItem(i));
        }

        // Sort the list by title
        Collections.sort(recipes, (o1, o2) -> (o1.getPreparation_time() - o2.getPreparation_time()));

        // Check if top items are same
        Recipe recipe = (Recipe) recipe_list_adapter.getItem(0);

        Assert.assertEquals(recipe.getPreparation_time(), recipes.get(0).getPreparation_time());


        // Launch sort fragment
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_recipes));

        // Wait for fragment to pop up
        solo.sleep(1000);

        // Click order by High to Low and sort by prep time
        solo.clickOnButton("High to Low");
        solo.sleep(1000);
        solo.clickOnText("Ok");
        solo.sleep(1000);


        // Get all recipes displayed
        recipes = new ArrayList<>();
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            recipes.add((Recipe) recipe_list_adapter.getItem(i));
        }

        // Sort the list by title
        Collections.sort(recipes, (o1, o2) -> (o2.getPreparation_time() - o1.getPreparation_time()));

        // Check if top items are same
        recipe = (Recipe) recipe_list_adapter.getItem(0);

        Assert.assertEquals(recipe.getPreparation_time(), recipes.get(0).getPreparation_time());
    }

    /**
     * Tests the sorting functionality of the recipes (Sort by Number of servings)
     */
    @Test
    public void testSortByServingsRecipes() throws InterruptedException {

        // Launch sort fragment
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_recipes));

        // Wait for fragment to pop up
        solo.sleep(1000);

        // Click OK to test order by Low to High and sort by servings sort
        solo.clickOnText("Ok");
        solo.sleep(1000);

        // Check if top recipe is Ramen
        ListView recipe_list_view = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter = recipe_list_view.getAdapter();

        // Get all recipes displayed
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            recipes.add((Recipe) recipe_list_adapter.getItem(i));
        }

        // Sort the list by servings
        Collections.sort(recipes, (o1, o2) -> (o1.getNum_servings() - o2.getNum_servings()));

        // Check if top items are same
        Recipe recipe = (Recipe) recipe_list_adapter.getItem(0);

        Assert.assertEquals(recipe.getNum_servings(), recipes.get(0).getNum_servings());


        // Launch sort fragment
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_recipes));

        // Wait for fragment to pop up
        solo.sleep(1000);

        // Click order by High to Low and sort by Title
        solo.clickOnButton("High to Low");
        solo.sleep(1000);
        solo.clickOnText("Ok");
        solo.sleep(1000);


        // Get all recipes displayed
        recipes = new ArrayList<>();
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            recipes.add((Recipe) recipe_list_adapter.getItem(i));
        }

        // Sort the list by servings
        Collections.sort(recipes, (o1, o2) -> (o2.getNum_servings() - o1.getNum_servings()));

        // Check if top items are same
        recipe = (Recipe) recipe_list_adapter.getItem(0);

        Assert.assertEquals(recipe.getNum_servings(), recipes.get(0).getNum_servings());
    }

    /**
     * Tests the sorting functionality of the recipes (Sort by Category)
     */
    @Test
    public void testSortByCategoryRecipes() throws InterruptedException {

        // Launch sort fragment
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_recipes));

        // Wait for fragment to pop up
        solo.sleep(1000);

        // Click OK to test order by Low to High and sort by category sort
        solo.clickOnText("Ok");
        solo.sleep(1000);

        // Check if top recipe is Ramen
        ListView recipe_list_view = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter = recipe_list_view.getAdapter();

        // Get all recipes displayed
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            recipes.add((Recipe) recipe_list_adapter.getItem(i));
        }

        // Sort the list by title
        Collections.sort(recipes, (o1, o2) -> (o1.getCategory().toLowerCase().compareTo(o2.getCategory().toLowerCase())));

        // Check if top items are same
        Recipe recipe = (Recipe) recipe_list_adapter.getItem(0);

        Assert.assertEquals(recipe.getCategory(), recipes.get(0).getCategory());


        // Launch sort fragment
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.sort_recipes));

        // Wait for fragment to pop up
        solo.sleep(1000);

        // Click order by High to Low and sort by category
        solo.clickOnButton("High to Low");
        solo.sleep(1000);
        solo.clickOnText("Ok");
        solo.sleep(1000);


        // Get all recipes displayed
        recipes = new ArrayList<>();
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            recipes.add((Recipe) recipe_list_adapter.getItem(i));
        }

        // Sort the list by category
        Collections.sort(recipes, (o1, o2) -> (o2.getCategory().toLowerCase().compareTo(o1.getCategory().toLowerCase())));

        // Check if top items are same
        recipe = (Recipe) recipe_list_adapter.getItem(0);

        Assert.assertEquals(recipe.getCategory(), recipes.get(0).getCategory());
    }

    /**
     * Test viewing a recipe
     */
    @Test
    public void testViewRecipe() {

        // Current activity
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);

        // Get the top item
        ListView recipe_list_view = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter = recipe_list_view.getAdapter();
        Recipe topRecipe = (Recipe) recipe_list_adapter.getItem(0);

        // click on view
        solo.clickOnText("View");
        solo.sleep(1000);

        // Make sure RecipeViewActivity launches
        solo.assertCurrentActivity("Wrong activity", ViewRecipeActivity.class);
        solo.waitForText(topRecipe.getTitle());

        // Fetch all texts
        MaterialTextView categoryText = (MaterialTextView) solo.getView(R.id.recipe_detail_category);
        Assert.assertEquals(categoryText.getText(), topRecipe.getCategory());
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
