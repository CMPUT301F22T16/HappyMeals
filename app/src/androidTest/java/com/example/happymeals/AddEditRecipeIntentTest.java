package com.example.happymeals;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.recipe.EditRecipe;
import com.example.happymeals.recipe.RecipeAddComment;
import com.example.happymeals.recipe.RecipeAddIngredient;
import com.example.happymeals.recipe.RecipeEditComment;
import com.example.happymeals.recipe.RecipeEditIngredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddEditRecipeIntentTest {
    private Solo solo;
    private FirebaseFirestore conn;
    private String r_id;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnView(solo.getView(R.id.recipe_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
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
     * This test will check that the user can add a new recipe. This tests fills in all
     * the fields for creating a new recipe except for adding a photo. The test will add
     * the recipe to the firestore. This test also checks that the user can edit a recipe.
     * The recipe that was just created will be edited. All the fields will be edited and
     * the edited recipe will be submitted to firebase. This test also checks that the
     * user can delete the recipe. The test does this by first deleting all the comments
     * and ingredients created earlier. Then the test will delete the recipe.
     * @throws InterruptedException
     */
    @Test
    public void testRecipeActivity() throws InterruptedException {
        ///// ----- Test that the user can add a new recipe ----- /////
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.add_storage_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Enter values into the EditText fields
        solo.enterText((EditText) solo.getView(R.id.recipe_title_edit_text), "Ramen");
        solo.enterText((EditText) solo.getView(R.id.recipe_prep_time_edit_text), "20");
        solo.enterText((EditText) solo.getView(R.id.recipe_num_serv_edit_text), "20");
        solo.enterText((EditText) solo.getView(R.id.recipe_category_edit_text), "Soup");

        // Add a new comment
        solo.clickOnView(solo.getView(R.id.recipe_view_comments_button));
        solo.clickOnView(solo.getView(R.id.recipe_add_new_comment_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeAddComment.class);
        solo.enterText((EditText) solo.getView(R.id.recipe_add_comment_editText), "Delicious!");
        solo.clickOnView(solo.getView(R.id.recipe_add_comment_submit_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
        solo.waitForText("Delicious!");
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Add a new ingredient
        solo.clickOnView(solo.getView(R.id.recipe_view_ingredients_button));
        solo.clickOnView(solo.getView(R.id.recipe_pick_new_ingredient_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeAddIngredient.class);
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_description), "Noodles");
        solo.pressSpinnerItem(0, 0);
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_amount), "20");
        solo.pressSpinnerItem(1, 0);
        solo.clickOnView(solo.getView(R.id.recipe_add_ingredient_btn));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
//        solo.clickOnView(solo.getView(R.id.recipe_view_ingredients_button));
        solo.waitForText("Noodles");
        solo.clickOnButton("OK");

        // Upload new recipe to firebase
        solo.clickOnView(solo.getView(R.id.recipe_submit_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);

        ///// ----- Test that the user can edit the newly created recipe ----- /////
        // Find the recipe that was just created.
        ListView recipe_list_view = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter = recipe_list_view.getAdapter();
        int index = -1;  // This variable stores the index position of the recipe that was just added.
        // Get the index position of the newly created recipe
        for (int i = 0; i < recipe_list_adapter.getCount(); i++) {
            Recipe item = (Recipe) recipe_list_adapter.getItem(i);
            if (
                    item.getTitle().equals("Ramen") &&
                            item.getPreparation_time() == 20 &&
                            item.getNum_servings() == 20 &&
                            item.getCategory().equals("Soup") &&
                            item.getComments().size() > 0 &&
                            item.getComments().get(0).equals("Delicious!") &&
                            item.getIngredients().size() > 0 &&
                            item.getIngredients().get(0).getDescription().equals("Noodles") &&
                            item.getIngredients().get(0).getCategory().equals("Vegetable") &&
                            item.getIngredients().get(0).getAmount().equals(20.00) &&
                            item.getIngredients().get(0).getUnits().equals("g")
            ) {
                index = i;
                break;
            }
        }
        assertTrue(index >= 0);  // Assert that the newly created recipe was found
        View recipe_entity =  recipe_list_view.getChildAt(index);
        FloatingActionButton recipe_edit_button = (FloatingActionButton) recipe_entity.findViewById(R.id.recipe_card_edit);
        solo.clickOnView(recipe_edit_button);
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Edit Values in the EditText fields
        EditText edit_text_var;
        solo.enterText((EditText) solo.getView(R.id.recipe_title_edit_text), " 2");
        edit_text_var = (EditText) solo.getView(R.id.recipe_prep_time_edit_text);
        edit_text_var.setText("");
        solo.enterText(edit_text_var, "10");
        edit_text_var = (EditText) solo.getView(R.id.recipe_num_serv_edit_text);
        edit_text_var.setText("");
        solo.enterText(edit_text_var, "10");
        edit_text_var = (EditText) solo.getView(R.id.recipe_category_edit_text);
        edit_text_var.setText("");
        solo.enterText(edit_text_var, "Japanese");

        // Edit the comment
        solo.clickOnView(solo.getView(R.id.recipe_view_comments_button));
        RecyclerView comment_recycler_view = (RecyclerView) solo.getView(R.id.recipe_comments_recyclerview);
        View comment_entity = comment_recycler_view.getChildAt(0);
        ImageButton edit_comment_btn = (ImageButton) comment_entity.findViewById(R.id.edit_recipe_ingredient_btn);
        solo.clickOnView(edit_comment_btn);
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeEditComment.class);
        edit_text_var = (EditText) solo.getView(R.id.recipe_edit_comment_editText);
        edit_text_var.setText("");
        solo.enterText(edit_text_var, "Amazing!");
        solo.clickOnView((Button) solo.getView(R.id.recipe_edit_comment_submit_button));
        solo.sleep(3000);
        solo.waitForText("Amazing!");
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Edit the Ingredient
        solo.clickOnView(solo.getView(R.id.recipe_view_ingredients_button));
        RecyclerView ingredient_recycler_view = (RecyclerView) solo.getView(R.id.recipe_ingredient_recyclerview);
        View ingredient_entity = ingredient_recycler_view.getChildAt(0);
        ImageButton edit_ingredient_btn = (ImageButton) ingredient_entity.findViewById(R.id.edit_recipe_ingredient_btn);
        solo.clickOnView(edit_ingredient_btn);
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeEditIngredient.class);
        EditText edit_desc = (EditText) solo.getView(R.id.recipe_edit_ingredient_description);
//        EditText edit_category = (EditText) solo.getView(R.id.recipe_edit_ingredient_category);
        solo.pressSpinnerItem(0, 4);
        EditText edit_amount = (EditText) solo.getView(R.id.recipe_edit_ingredient_amount);
        solo.enterText(edit_desc, " 2");
        edit_amount.setText("");
        solo.enterText(edit_amount, "10");
        solo.pressSpinnerItem(1, 0);
        solo.clickOnView((Button) solo.getView(R.id.recipe_edit_ingredient_save_btn));
        solo.sleep(3000);
        solo.waitForText("Noodles 2");
        solo.clickOnButton("OK");
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Upload edited recipe to firebase
        solo.clickOnView(solo.getView(R.id.recipe_submit_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);

        ///// ----- Test that the user can delete comments and ingredients from the recipe ----- /////
        // Find the recipe that was just created.
        ListView recipe_list_view_2 = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter_2 = recipe_list_view_2.getAdapter();
        int index_2 = -1;  // This variable stores the index position of the recipe that was just added.
        // Get the index position of the newly edited recipe
        for (int i = 0; i < recipe_list_adapter_2.getCount(); i++) {
            Recipe item = (Recipe) recipe_list_adapter_2.getItem(i);
            if (
                    item.getTitle().equals("Ramen 2") &&
                            item.getPreparation_time() == 10 &&
                            item.getNum_servings() == 10 &&
                            item.getCategory().equals("Japanese") &&
                            item.getComments().size() > 0 &&
                            item.getComments().get(0).equals("Amazing!") &&
                            item.getIngredients().size() > 0 &&
                            item.getIngredients().get(0).getDescription().equals("Noodles 2") &&
                            item.getIngredients().get(0).getCategory().equals("Dry food") &&
                            item.getIngredients().get(0).getAmount().equals(10.00) &&
                            item.getIngredients().get(0).getUnits().equals("g")
            ) {
                index_2 = i;
                break;
            }
        }
        assertTrue(index_2 >= 0);  // Assert that the newly created recipe was found
        View recipe_entity_2 =  recipe_list_view_2.getChildAt(index_2);
        FloatingActionButton recipe_edit_button_2 = (FloatingActionButton) recipe_entity_2.findViewById(R.id.recipe_card_edit);
        solo.clickOnView(recipe_edit_button_2);
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Delete comment
        solo.clickOnView(solo.getView(R.id.recipe_view_comments_button));
        comment_entity = comment_recycler_view.getChildAt(0);
        ImageButton delete_comment_btn = (ImageButton) comment_entity.findViewById(R.id.delete_recipe_ingredient_btn);
        solo.clickOnView(delete_comment_btn);
        onView(withId(R.id.recipe_comments_recyclerview))
                .check(matches(not(hasDescendant(withText("Amazing!")))));
        solo.clickOnButton("OK");

        // Delete ingredient
        solo.clickOnView(solo.getView(R.id.recipe_view_ingredients_button));
        ingredient_entity = ingredient_recycler_view.getChildAt(0);
        ImageButton delete_ingredient_btn = (ImageButton) ingredient_entity.findViewById(R.id.delete_recipe_ingredient_btn);
        solo.clickOnView(delete_ingredient_btn);
        onView(withId(R.id.recipe_ingredient_recyclerview))
                .check(matches(not(hasDescendant(withText("Noodles 2")))));
        solo.clickOnButton("OK");

        // Save changes to firebase
        solo.clickOnView(solo.getView(R.id.recipe_submit_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);

        // Delete the recipe
        ListView recipe_list_view_3 = (ListView) solo.getView(R.id.recipe_list);
        ListAdapter recipe_list_adapter_3 = recipe_list_view_3.getAdapter();
        int index_3 = -1;  // This variable stores the index position of the recipe that was just added.
        // Get the index position of the newly created recipe
        for (int i = 0; i < recipe_list_adapter_3.getCount(); i++) {
            Recipe item = (Recipe) recipe_list_adapter_3.getItem(i);
            if (
                    item.getTitle().equals("Ramen 2") &&
                            item.getPreparation_time() == 10 &&
                            item.getNum_servings() == 10 &&
                            item.getCategory().equals("Japanese") &&
                            item.getComments().size() == 0 &&
                            item.getIngredients().size() == 0
            ) {
                index_3 = i;
                break;
            }
        }
        assertTrue(index_3 >= 0);  // Assert that the newly created recipe was found
        View recipe_entity_3 =  recipe_list_view_3.getChildAt(index_3);
        int recipeCountBefore = recipe_list_adapter_3.getCount();
        FloatingActionButton recipe_delete_button = (FloatingActionButton) recipe_entity_3.findViewById(R.id.recipe_card_delete);
        solo.clickOnView(recipe_delete_button);
        solo.clickOnButton("Yes");
        solo.sleep(3000);
        int recipeCountAfter = recipe_list_adapter_3.getCount();
        assertEquals(1, recipeCountBefore - recipeCountAfter);
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
