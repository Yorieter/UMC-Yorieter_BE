package umc.yorieter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.yorieter.domain.SearchLog;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchLogResponseDTO {
    private Long id;
    private String searchLogName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberId;

    public static SearchLogResponseDTO fromSearchLog(SearchLog searchLog) {
        return SearchLogResponseDTO.builder()
                .id(searchLog.getId())
                .memberId(searchLog.getMember().getId())
                .searchLogName(searchLog.getSearchLogName())
                .createdAt(searchLog.getCreatedAt())
                .updatedAt(searchLog.getUpdatedAt())
                .build();
    }
}
