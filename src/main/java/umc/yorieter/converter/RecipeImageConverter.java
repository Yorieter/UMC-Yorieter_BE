package umc.yorieter.converter;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.RecipeImage;
import umc.yorieter.repository.RecipeRepository;
import umc.yorieter.web.dto.request.RecipeImageRequestDto;
import umc.yorieter.web.dto.response.RecipeImageResponseDto;

@Component
@RequiredArgsConstructor
public class RecipeImageConverter {

    private final RecipeRepository recipeRepository;

    public RecipeImage toRecipeImage(String imageUrl, RecipeImageRequestDto request) {
        System.out.println(recipeRepository.findAll());
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new EntityNotFoundException("Recipe with ID " + request.getRecipeId() + " not found"));

        return RecipeImage.builder()
                .url(imageUrl)
                .recipe(recipe)
                .build();
    }

    public RecipeImageResponseDto.RecipeImageDto toRecipeImageDto(RecipeImage recipeImage) {
        return RecipeImageResponseDto.RecipeImageDto.builder()
                .url(recipeImage.getUrl())
                .recipeId(recipeImage.getRecipe().getId())
                .build();
    }
}
