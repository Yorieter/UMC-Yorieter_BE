package umc.yorieter.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import umc.yorieter.domain.Ingredient;
import umc.yorieter.domain.mapping.Recipe_Ingredient;
import umc.yorieter.domain.Recipe;
import umc.yorieter.repository.*;
import umc.yorieter.web.dto.request.SearchRequestDTO;

import umc.yorieter.web.dto.response.SearchResponseDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;


    @Override
    public SearchResponseDTO.AllRecipeListDto getAllRecipes(SearchRequestDTO.SearchRecipeDTO searchRequestDTO) {
        Integer maxCalorie = searchRequestDTO.getMaxCalorie();
        Integer minCalorie = searchRequestDTO.getMinCalorie();
        List<String> ingredientNames = searchRequestDTO.getIngredientNames();

        log.info("Search Request - Max Calorie: {}, Min Calorie: {}, Ingredient Names: {}", maxCalorie, minCalorie, ingredientNames);


        // 1. 식재료 이름으로 ingredient_id 찾기
       List<Long> ingredientIds = ingredientNames.stream()
                .map(ingredientName -> ingredientRepository.findByName(ingredientName)
                        .map(Ingredient::getId)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
        log.info("Ingredients: {}", ingredientIds);  // 식재료 이름 찾는 메소드

        // 2. ingredient_id로 recipe_id 찾기
        List<Long> recipeIds = ingredientIds.stream()
                .flatMap(ingredientId -> recipeIngredientRepository.findByIngredientId(ingredientId).stream()
                        .map(recipeIngredient -> recipeIngredient.getRecipe().getId()))
                .distinct()
                .collect(Collectors.toList());

        log.info("Recipe IDs: {}", recipeIds);

        // 3. recipe_id로 Recipe 엔티티 찾기
        List<Recipe> recipes = recipeRepository.findByIdIn(recipeIds);
        log.info("Recipe IDs: {}", recipeIds);

        // 필터링: 칼로리 범위에 맞는 레시피만 선택
        List<Recipe> filteredRecipes = recipes.stream()
                .filter(recipe -> recipe.getCalories() >= minCalorie && recipe.getCalories() <= maxCalorie)
                .toList();

        log.info("Filtered Recipes Count: {}", filteredRecipes.size());


        // DTO 변환
        List<SearchResponseDTO.DetailRecipeDTO> detailRecipeDTOS = filteredRecipes.stream()
                .map(recipe -> SearchResponseDTO.DetailRecipeDTO.builder()
                        .recipeId(recipe.getId())
                        .memberId(recipe.getMember().getId())
                        .title(recipe.getTitle())
                        .description(recipe.getDescription())
                        .calories(recipe.getCalories())
                        .imageUrl(recipe.getRecipeImage().getUrl())
                        .ingredientNames(ingredientNames)
                        .createdAt(recipe.getCreatedAt())
                        .updatedAt(recipe.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        log.info("Detail Recipe DTOs Count: {}", detailRecipeDTOS.size());


        return SearchResponseDTO.AllRecipeListDto.builder()
                .recipeList(detailRecipeDTOS)
                .build();

    }
}