package com.example.happymeals;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.recipe.EditRecipe;
import com.example.happymeals.recipe.RecipeAddComment;
import com.example.happymeals.recipe.RecipeAddIngredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RecipeIntentTest {
    private Solo solo;
    private FirebaseFirestore conn;
    private String r_id;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnView(solo.getView(R.id.recipe_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testRecipeActivity() throws InterruptedException {
        ///// ----- Test that the user can add a new recipe ----- /////
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);

        // Enter values into the EditText fields
        solo.enterText((EditText) solo.getView(R.id.recipe_title_edit_text), "Ramen");
        solo.enterText((EditText) solo.getView(R.id.recipe_prep_time_edit_text), "20");
        solo.enterText((EditText) solo.getView(R.id.recipe_num_serv_edit_text), "20");
        solo.enterText((EditText) solo.getView(R.id.recipe_category_edit_text), "Soup");

        // Add a new comment
        solo.clickOnView(solo.getView(R.id.recipe_add_new_comment_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeAddComment.class);
        solo.enterText((EditText) solo.getView(R.id.recipe_add_comment_editText), "Delicious!");
        solo.clickOnView(solo.getView(R.id.recipe_add_comment_submit_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
        solo.waitForText("Delicious!");

        // Add a new ingredient
        solo.clickOnView(solo.getView(R.id.recipe_pick_new_ingredient_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeAddIngredient.class);
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_description), "Noodles");
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_category), "Grain");
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_amount), "20");
        solo.clickOnView(solo.getView(R.id.recipe_add_ingredient_btn));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
        solo.waitForText("Noodles");

        // Upload new recipe to firebase
        solo.clickOnView(solo.getView(R.id.recipe_submit_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);

        ///// ----- Test that the user can edit the newly created recipe ----- /////
        // Find the recipe that was just created.
        List<Recipe> recipe_list;
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
                            item.getIngredients().get(0).getCategory().equals("Grain") &&
                            item.getIngredients().get(0).getAmount().equals(20.00)
            ) {
                index = i;
                break;
            }
        }
        assertTrue(index >= 0);  // Assert that the newly created recipe was found
//        View recipe_entity =  recipe_list_view.getChildAt(index);
//        FloatingActionButton recipe_edit_button = (FloatingActionButton) solo.getView(R.id.recipe_card_edit);
//        FloatingActionButton recipe_edit_button = (FloatingActionButton) recipe_entity.findViewById(R.id.recipe_card_edit);
//        solo.clickOnView(recipe_edit_button);
//        solo.sleep(3000);
//        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
//
//        // Edit Values in the EditText fields
//        EditText edit_text_var;
//        solo.enterText((EditText) solo.getView(R.id.recipe_title_edit_text), " 2");
//        edit_text_var = (EditText) solo.getView(R.id.recipe_prep_time_edit_text);
//        edit_text_var.setText("");
//        solo.enterText(edit_text_var, "10");
//        edit_text_var = (EditText) solo.getView(R.id.recipe_num_serv_edit_text);
//        edit_text_var.setText("");
//        solo.enterText(edit_text_var, "10");
//        edit_text_var = (EditText) solo.getView(R.id.recipe_category_edit_text);
//        edit_text_var.setText("");
//        solo.enterText(edit_text_var, "Japanese");
//
//        // Edit the comment
//        RecyclerView comment_recycler_view = (RecyclerView) solo.getView(R.id.recipe_comments_recyclerview);
//        comment_recycler_view.getChildAt(0);
//        Button edit_comment_btn = (Button) solo.getView(R.id.edit_recipe_ingredient_btn);

    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
