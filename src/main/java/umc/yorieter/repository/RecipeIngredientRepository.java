package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Ingredient;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.Recipe_Ingredient;

import java.util.List;


public interface RecipeIngredientRepository extends JpaRepository<Recipe_Ingredient, Long> {
    boolean existsByRecipeAndIngredient(Recipe recipe, Ingredient ingredient);
}