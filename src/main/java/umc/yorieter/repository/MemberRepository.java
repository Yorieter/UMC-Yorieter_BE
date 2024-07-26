package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
