package umc.yorieter.service.SearchService;
import umc.yorieter.web.dto.request.SearchRequestDTO;
import umc.yorieter.web.dto.response.SearchResponseDTO;

public interface SearchService {

    // 레시피 전체 조회
    SearchResponseDTO.AllRecipeListDto getAllRecipes(SearchRequestDTO.SearchRecipeDTO searchRequestDTO);
}