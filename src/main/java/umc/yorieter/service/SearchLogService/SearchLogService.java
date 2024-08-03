package umc.yorieter.service.SearchLogService;

import umc.yorieter.web.dto.response.SearchLogResponseDTO;

import java.util.List;

public interface SearchLogService {
    void saveRecentSearchLog(Long memberId, String name);
    List<SearchLogResponseDTO> findRecentSearchLogs(Long memberId);
}
