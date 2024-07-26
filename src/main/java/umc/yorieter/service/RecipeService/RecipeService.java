package umc.yorieter.service.RecipeService;


import umc.yorieter.web.dto.request.RecipeRequestDTO;
import umc.yorieter.web.dto.response.RecipeResponseDTO;

public interface RecipeService {

    // 레시피 작성
    Long createRecipe(Long memberId, RecipeRequestDTO.CreateRecipeDTO createRecipeDTO);

    // 레시피 (상세)조회
    RecipeResponseDTO.DetailRecipeDTO getRecipe(Long recipeId);

    // 레시피 수정
    void updateRecipe(Long memberId, Long recipeId, RecipeRequestDTO.UpdateRecipeDTO updateRecipeDTO);

    // 레시피 삭제
    void deleteRecipe(Long recipeId);

    // 레시피 좋아요
    void addLike(Long memberId, Long recipeId);

    // 레시피 좋아요 해제
    void deleteLike(Long memberId, Long recipeId);
}
