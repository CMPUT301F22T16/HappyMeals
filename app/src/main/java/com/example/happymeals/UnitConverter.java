package com.example.happymeals;

import com.example.happymeals.meal.Meal;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UnitConverter {
    public static final List<String> upperList = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("l");
                add("kg");
                // etc
            }});
    public static final List<String> lowerList = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("ml");
                add("g");
                // etc
            }});

    public static final List<String> p3List = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("l");
                add("kg");
                // etc
            }});

    public static final List<String> p2List = Collections.unmodifiableList(
            new ArrayList<String>() {{
                // etc
            }});

    public static final List<String> p1List = Collections.unmodifiableList(
            new ArrayList<String>() {{
                // etc
            }});

    public static final List<String> t3List = Collections.unmodifiableList(
            new ArrayList<String>() {{
                // etc
            }});

    /**
     * This function builds a shoppinglist based on the ingredients required by the mealplan
     * and the ingredients currently owned by user
     * @param mealPlan
     * @param userIngredients
     * @return
     */
    static public ArrayList<RecipeIngredient> getShoppingList(MealPlan mealPlan, List<UserIngredient> userIngredients) {
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
        ArrayList<RecipeIngredient> tempIngredients = new ArrayList<>();
        ArrayList<RecipeIngredient> returnIngredients = new ArrayList<>();
        for (List<Meal> meals : mealPlan.getMeals()) {
            for (Meal meal : meals) {
                for (Recipe recipe : meal.getRecipes()) {
                    recipeIngredients.addAll(recipe.getIngredients());
                }
            }
        }
        //  get rid of the duplicates in the list
        for (RecipeIngredient origIng : recipeIngredients) {
            boolean containsFlag = false;
            for (RecipeIngredient newIng : tempIngredients) {
                if (origIng.getDescription() == newIng.getDescription() && origIng.getCategory() == newIng.getCategory()) {
                    RtoRIngredientUpdate(newIng, origIng);
                    containsFlag = true;
                }
            }

            if(!containsFlag) {
                tempIngredients.add(origIng);
            }
        }
        //  adjust recipeingredients amount according to useringredients
        for (Ingredient ingredient : userIngredients) {
            for (Ingredient ringredient : tempIngredients) {
                if (ingredient.getDescription().toLowerCase().equals(ringredient.getDescription().toLowerCase()) && ingredient.getCategory().toLowerCase().equals(ringredient.getCategory().toLowerCase())) {
                    UtoRIngredientUpdate(ringredient, ingredient);
                }
            }
        }
        //  remove ingredient with amount <= 0
        for (RecipeIngredient ingredient : tempIngredients) {
            if (ingredient.getAmount()>0) {
                returnIngredients.add(ingredient);
            }
        }
        return returnIngredients;
    }

    /**
     * Update r1's amount to the sum of r1 and r2 keeping the larger unit of the two
     * @param r1
     * @param r2
     */
    static public void RtoRIngredientUpdate(Ingredient r1, Ingredient r2) {
        if (r1.getUnit() == r2.getUnit()) {
            r1.setAmount(r1.getAmount()+r2.getAmount());
        } else if (upperList.contains(r1.getUnit()) && lowerList.contains(r2.getUnit())) {
            StoLKeepL(r1, r2);
        } else if (lowerList.contains(r1.getUnit()) && upperList.contains(r2.getUnit())) {
            LtoSKeepL(r1, r2);
        }
    }

    /**
     * Update r1's amount to r1 - r2 keeping unit of the r1
     * @param r1
     * @param r2
     */
    static public void UtoRIngredientUpdate(Ingredient r1, Ingredient r2) {
        if (r1.getUnit().toLowerCase().equals(r2.getUnit().toLowerCase()) ) {
            r1.setAmount(r1.getAmount()-r2.getAmount());
        } else if (upperList.contains(r1.getUnit()) && lowerList.contains(r2.getUnit())) {
            NegStoLKeepL(r1, r2);
        } else if (lowerList.contains(r1.getUnit()) && upperList.contains(r2.getUnit())) {
            NegLtoSKeepS(r1, r2);
        }
    }

    /**
     * This function coverts the sum of two amount to the Larger unit and stored it in r1
     * @param r1
     * @param r2
     */
    static public void StoLKeepL(Ingredient r1, Ingredient r2) {
        if (p3List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()+r2.getAmount()/1000);
        } else if (p2List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()+r2.getAmount()/100);
        } else if (p1List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()+r2.getAmount()/10);
        } else if (t3List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()+r2.getAmount()/3);
        }
    }

    /**
     * This function coverts the sum of two amount to the Larger unit and stored it in r1
     * @param r1
     * @param r2
     */
    static public void LtoSKeepL(Ingredient r1, Ingredient r2) {
        if (p3List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()/1000+r2.getAmount());
        } else if (p2List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()/100+r2.getAmount());
        } else if (p1List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()/10+r2.getAmount());
        } else if (t3List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()/3+r2.getAmount());
        }
        r1.setUnit(r2.getUnit());
    }

    /**
     * This function coverts the r1 - r2 to the Smaller unit and stored it in r1
     * @param r1
     * @param r2
     */
    static public void NegLtoSKeepS(Ingredient r1, Ingredient r2) {
        if (p3List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()-r2.getAmount()*1000);
        } else if (p2List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()-r2.getAmount()*100);
        } else if (p1List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()-r2.getAmount()*10);
        } else if (t3List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()-r2.getAmount()*3);
        }
    }

    /**
     * This function coverts the r1 - r2 to the Larger unit and stored it in r1
     * @param r1
     * @param r2
     */
    static public void NegStoLKeepL(Ingredient r1, Ingredient r2) {
        if (p3List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()-r2.getAmount()/1000);
        } else if (p2List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()-r2.getAmount()/100);
        } else if (p1List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()-r2.getAmount()/10);
        } else if (t3List.contains(r1.getUnit())) {
            r1.setAmount(r1.getAmount()-r2.getAmount()/3);
        }
    }
}


