package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.SearchLog;

import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
    List<SearchLog> findTop5ByMemberOrderByCreatedAtDesc(Member member);
}
