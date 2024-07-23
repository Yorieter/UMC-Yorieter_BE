package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
