package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Member;


import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String Username);
}

