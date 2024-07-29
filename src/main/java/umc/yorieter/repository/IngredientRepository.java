package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Ingredient;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // 식재료 이름 찾는 메소드
    Optional<Ingredient> findByName(String ingredientName);
}
