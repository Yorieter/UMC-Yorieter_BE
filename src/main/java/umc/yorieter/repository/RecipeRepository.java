package umc.yorieter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r JOIN FETCH r.recipeIngredientList ri JOIN FETCH ri.ingredient WHERE r.id = :recipeId")
    Recipe findRecipeWithIngredients(@Param("recipeId") Long recipeId);

    // @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.recipeIngredientList ri LEFT JOIN FETCH ri.ingredient")
    // List<Recipe> findAllWithIngredients();

    @Query("SELECT r FROM Recipe r " +
            "LEFT JOIN r.recipeIngredientList ri " +
            "LEFT JOIN ri.ingredient i " +
            "LEFT JOIN r.recipeLikeList rl " +
            "GROUP BY r.id, r.calories, r.createdAt, r.description, r.member.id, r.title, r.updatedAt " +
            "ORDER BY COUNT(rl) DESC")
    List<Recipe> findAllWithIngredientsSortedByLikes();

    Page<Recipe> findAllByMember(Member member, PageRequest pageRequest);

    List<Recipe> findByIdIn(List<Long> recipeIds);

    List<Recipe> findByTitleContaining(String title);
}
