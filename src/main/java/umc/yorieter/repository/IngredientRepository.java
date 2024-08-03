package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Ingredient;
import umc.yorieter.domain.Recipe;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // 식재료 이름 찾는 메소드
    Optional<Ingredient> findByName(String ingredientName);



    //List<Ingredient> findAllByName(List<String> ingredientNames);
}
