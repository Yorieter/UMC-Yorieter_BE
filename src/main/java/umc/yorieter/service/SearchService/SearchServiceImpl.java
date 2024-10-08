package umc.yorieter.service.SearchService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import umc.yorieter.domain.Ingredient;
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


    @Transactional
    @Override
    public SearchResponseDTO.AllRecipeListDto getAllRecipes(SearchRequestDTO.SearchRecipeDTO searchRequestDTO) {
        List<String> ingredientNames = searchRequestDTO.getIngredientNames();

        // 1. 식재료 이름으로 ingredient_id 찾기
        List<Long> ingredientIds = ingredientNames.stream()
                .map(ingredientName -> ingredientRepository.findByName(ingredientName)
                        .map(Ingredient::getId)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();


        // 2. ingredient_id로 recipe_id 찾기
        List<Long> recipeIds = ingredientIds.stream()
                .flatMap(ingredientId -> recipeIngredientRepository.findByIngredientId(ingredientId).stream()
                        .map(recipeIngredient -> recipeIngredient.getRecipe().getId()))
                .distinct()
                .collect(Collectors.toList());



        // 3. recipe_id로 Recipe 엔티티 찾기
        List<Recipe> recipes = recipeRepository.findByIdIn(recipeIds);


        // 필터링: 칼로리 범위에 맞는 레시피만 선택
        //List<Recipe> filteredRecipes = recipes.stream()
        //        .filter(recipe ->recipe.getCalories()!= null &&  recipe.getCalories() >= minCalorie && recipe.getCalories() <= maxCalorie)
        //        .toList();



        // DTO 변환
        List<SearchResponseDTO.DetailRecipeDTO> detailRecipeDTOS = recipes.stream()
                .map(recipe -> {
                    Integer calories = recipe.getCalories() != null ? recipe.getCalories() : 0; // 기본값 0 설정
                    // 식재료 리스트 가져오기
                    List<String> recipeIngredientNames = recipe.getRecipeIngredientList().stream()
                            .map(recipeIngredient -> recipeIngredient.getIngredient().getName()) // 각 재료의 이름을 추출
                            .collect(Collectors.toList());

                    String imageUrl = recipe.getRecipeImage() != null ? recipe.getRecipeImage().getUrl() : "https://umc-yorieter.s3.ap-northeast-2.amazonaws.com/default.png";

                    return SearchResponseDTO.DetailRecipeDTO.builder()
                            .recipeId(recipe.getId())
                            .memberId(recipe.getMember().getId())
                            .title(recipe.getTitle())
                            .description(recipe.getDescription())
                            .calories(calories)
                            .imageUrl(imageUrl) // recipe.getRecipeImage().getUrl()
                            .ingredientNames(recipeIngredientNames) // 추출한 식재료 이름 리스트 사용
                            .createdAt(recipe.getCreatedAt())
                            .updatedAt(recipe.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());



        return SearchResponseDTO.AllRecipeListDto.builder()
                .recipeList(detailRecipeDTOS)
                .build();

    }

    @Override
    @Transactional
    public SearchResponseDTO.AllRecipeListDto searchByTitle(SearchRequestDTO.TitleSearchDTO titleSearchDTO) {
        String title = titleSearchDTO.getTitle();


        // 제목으로 레시피 검색
        List<Recipe> recipes = recipeRepository.findByTitleContaining(title);



        // DTO 변환
        List<SearchResponseDTO.DetailRecipeDTO> detailRecipeDTOS = recipes.stream()
                .map(recipe -> {
                    List<String> recipeIngredientNames = recipe.getRecipeIngredientList().stream()
                            .map(recipeIngredient -> recipeIngredient.getIngredient().getName())
                            .collect(Collectors.toList());

                    String imageUrl = recipe.getRecipeImage() != null ? recipe.getRecipeImage().getUrl() : "https://umc-yorieter.s3.ap-northeast-2.amazonaws.com/default.png";

                    return SearchResponseDTO.DetailRecipeDTO.builder()
                            .recipeId(recipe.getId())
                            .memberId(recipe.getMember().getId())
                            .title(recipe.getTitle())
                            .description(recipe.getDescription())
                            .calories(recipe.getCalories())
                            .imageUrl(imageUrl) // recipe.getRecipeImage().getUrl()
                            .ingredientNames(recipeIngredientNames)
                            .createdAt(recipe.getCreatedAt())
                            .updatedAt(recipe.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return SearchResponseDTO.AllRecipeListDto.builder()
                .recipeList(detailRecipeDTOS)
                .build();
    }

}