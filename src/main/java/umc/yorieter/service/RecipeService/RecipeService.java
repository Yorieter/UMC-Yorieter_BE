package umc.yorieter.service.RecipeService;


import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.web.dto.request.RecipeRequestDTO;
import umc.yorieter.web.dto.response.RecipeResponseDTO;
import umc.yorieter.web.dto.response.IngredientResponseDTO;

public interface RecipeService {

    // 레시피 작성
    RecipeResponseDTO.DetailRecipeDTO createRecipe(RecipeRequestDTO.CreateRecipeDTO createRecipeDTO, MultipartFile image);

    // 레시피 전체 조회
    RecipeResponseDTO.AllRecipeListDto getAllRecipes();

    // 레시피 (상세)조회
    RecipeResponseDTO.DetailRecipeDTO getRecipe(Long recipeId);

    // 레시피 수정
    RecipeResponseDTO.DetailRecipeDTO updateRecipe(Long recipeId, RecipeRequestDTO.UpdateRecipeDTO updateRecipeDTO, MultipartFile image);

    // 레시피 삭제
    void deleteRecipe(Long recipeId);

    // 레시피 좋아요
    void addLike(Long recipeId);

    // 레시피 좋아요 해제
    void deleteLike(Long recipeId);

    IngredientResponseDTO.IngredientDto searchIngredient(String name, Long recipeId);
}