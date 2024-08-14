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

    // 필터링 검색 레시피 조회
    @Operation(summary = "필터링 검색 API ", description = "식재료, 칼로리에 해당하는 레시피 리스트를 조회합니다.")
    @PostMapping("")
    public ApiResponse<SearchResponseDTO.AllRecipeListDto> getRecipeList(@RequestBody SearchRequestDTO.SearchRecipeDTO searchRequestDTO){
        return ApiResponse.onSuccess(searchService.getAllRecipes(searchRequestDTO));
    }

    // 제목 검색 레시피 조회
    @Operation(summary = "제목 검색 API", description = "제목에 해당하는 레시피 리스트를 조회합니다.")
    @PostMapping("/title")
    public ApiResponse<SearchResponseDTO.AllRecipeListDto> getRecipeByTitle(@RequestBody SearchRequestDTO.TitleSearchDTO titleSearchDTO) {
        return ApiResponse.onSuccess(searchService.searchByTitle(titleSearchDTO));
    }
}
