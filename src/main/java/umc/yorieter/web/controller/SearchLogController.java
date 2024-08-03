package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.SearchLogService.SearchLogService;
import umc.yorieter.web.dto.response.SearchLogResponseDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search/recent-searches")
public class SearchLogController {
    private final SearchLogService searchLogService;

    @PostMapping
    @Operation(summary = "최근 검색어 저장 API")
    public ApiResponse<Void> saveRecentSearchLog(
            @RequestParam Long memberId,
            @RequestParam String name) {
        searchLogService.saveRecentSearchLog(memberId, name);
        return new ApiResponse<>(true, "SEARCHLOG200", "최근 검색어 저장에 성공하였습니다.", null);
    }

    @GetMapping
    @Operation(summary = "최근 검색어 조회 API")
    public ApiResponse<List<SearchLogResponseDTO>> findRecentSearchLogs(@RequestParam Long memberId) {
        List<SearchLogResponseDTO> recentSearchLogs = searchLogService.findRecentSearchLogs(memberId);
        return new ApiResponse<>(true, "SEARCHLOG200", "최근 검색어 조회에 성공하였습니다.", recentSearchLogs);
    }
}
