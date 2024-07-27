package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.RecipeImage;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {
}
