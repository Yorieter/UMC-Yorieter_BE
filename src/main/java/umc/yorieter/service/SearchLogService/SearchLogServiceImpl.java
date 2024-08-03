package umc.yorieter.service.SearchLogService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.SearchLog;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.repository.SearchLogRepository;
import umc.yorieter.web.dto.response.SearchLogResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchLogServiceImpl implements SearchLogService {
    private final SearchLogRepository searchLogRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void saveRecentSearchLog(Long memberId, String name) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 최근 검색어 추가 및 저장
        SearchLog searchLog = SearchLog.builder()
                .searchLogName(name)
                .member(member)
                .build();

        searchLogRepository.save(searchLog);

        // 최근 검색어 5개만 저장하고 조회 가능
        List<SearchLog> recentLogs = searchLogRepository.findTop5ByMemberOrderByCreatedAtDesc(member);
        if (recentLogs.size() > 5) {
            SearchLog oldestLog = recentLogs.get(recentLogs.size() - 1);
            searchLogRepository.deleteById(oldestLog.getId());
        }
    }

    @Override
    public List<SearchLogResponseDTO> findRecentSearchLogs(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found")); // Member가 없는 경우 예외 처리

        List<SearchLog> searchLogs = searchLogRepository.findTop5ByMemberOrderByCreatedAtDesc(member);
        return searchLogs.stream()
                .map(SearchLogResponseDTO::fromSearchLog)
                .collect(Collectors.toList());
    }
}
