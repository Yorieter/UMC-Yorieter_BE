package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.SearchService.SearchService;
import umc.yorieter.web.dto.request.SearchRequestDTO;
import umc.yorieter.web.dto.response.SearchResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    // 검색 레시피 조회
    @Operation(summary = "모든 레시피 조회 API ", description = "레시피 리스트를 조회합니다..")
    @PostMapping("")
    public ApiResponse<SearchResponseDTO.AllRecipeListDto> getRecipeList(@RequestBody SearchRequestDTO.SearchRecipeDTO searchRequestDTO){
        return ApiResponse.onSuccess(searchService.getAllRecipes(searchRequestDTO));
    }
}
