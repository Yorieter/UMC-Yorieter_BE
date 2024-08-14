package umc.yorieter.service.SearchService;
import jakarta.transaction.Transactional;
import umc.yorieter.web.dto.request.SearchRequestDTO;
import umc.yorieter.web.dto.response.SearchResponseDTO;

public interface SearchService {

    // 필터링 검색 조회
    SearchResponseDTO.AllRecipeListDto getAllRecipes(SearchRequestDTO.SearchRecipeDTO searchRequestDTO);

    // 제목 검색 조회
    SearchResponseDTO.AllRecipeListDto searchByTitle(SearchRequestDTO.TitleSearchDTO titleSearchDTO);
}