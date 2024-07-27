package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.RecipeService.RecipeService;
import umc.yorieter.web.dto.request.RecipeRequestDTO;
import umc.yorieter.web.dto.response.RecipeResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {

    // 1. 컨트롤러 ApiResponse 예쁘게 만들기.. 나중에


    private final RecipeService recipeService;

    // 레시피 작성
    @Operation(summary = "레시피 작성 API", description = "레시피를 작성합니다.")
    @PostMapping("")
    public ApiResponse<RecipeResponseDTO.DetailRecipeDTO> create(@RequestPart(value = "request") RecipeRequestDTO.CreateRecipeDTO request,
                                                                 @RequestPart(value = "image") MultipartFile image){
        return ApiResponse.onSuccess(recipeService.createRecipe(request, image));
    }

    // 모든레시피 조회 (생성일자순 정렬) 추후에 좋아요 순서로 변경 필요
    @Operation(summary = "모든 레시피 조회 API", description = "레시피 리스트를 조회합니다..")
    @GetMapping("")
    public ApiResponse<RecipeResponseDTO.AllRecipeListDto> getRecipeList(){
        return ApiResponse.onSuccess(recipeService.getAllRecipes());
    }

    // 레시피 (상세)조회
    @Operation(summary = "레시피 상세조회 API", description = "레시피를 상세조회합니다.")
    @GetMapping("/{recipeId}")
    public ApiResponse<RecipeResponseDTO.DetailRecipeDTO> getDetailRecipe(@PathVariable Long recipeId) {
        return ApiResponse.onSuccess(recipeService.getRecipe(recipeId));
    }


    // 레시피 수정 <- 이게 맞냐?
    @Operation(summary = "레시피 상세조회 API", description = "레시피를 수정합니다.")
    @PatchMapping("/{recipeId}")
    public ApiResponse<RecipeRequestDTO.UpdateRecipeDTO> updateRecipe
    (Long memberId, @PathVariable Long recipeId, @RequestBody RecipeRequestDTO.UpdateRecipeDTO updateRecipeDTO) {
        recipeService.updateRecipe(memberId, recipeId, updateRecipeDTO);

        return new ApiResponse<>(true, "COMMON201", "이 레시피를 수정합니다.", null);

    }


    // 레시피 삭제
    @Operation(summary = "레시피 삭제 API", description = "레시피를 삭제합니다.")
    @DeleteMapping("/{recipeId}")
    public ApiResponse<?> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return new ApiResponse<>(true, "COMMON200", "레시피가 삭제되었습니다.", recipeId);
    }


    // 레시피 좋아요
    @Operation(summary = "레시피 좋아요 API", description = "레시피 좋아요를 합니다.")
    @PostMapping("/{recipeId}/liked")
    public ApiResponse<?> addLike(@RequestParam  Long memberId, @PathVariable Long recipeId) {
        recipeService.addLike(memberId, recipeId);

        return new ApiResponse<>(true, "COMMON201", "이 레시피를 좋아합니다.", null);
    }


    // 레시피 좋아요 해제
    @Operation(summary = "레시피 좋아요 해제 API", description = "레시피 좋아요를 해제합니다.")
    @DeleteMapping("/{recipeId}/delete")
    public ApiResponse<?> deleteLike(@RequestParam Long memberId, @PathVariable Long recipeId){
        recipeService.deleteLike(memberId, recipeId);

        return new ApiResponse<>(true, "COMMON204", "레시피 좋아요를 해제했습니다.",null);
    }







}
