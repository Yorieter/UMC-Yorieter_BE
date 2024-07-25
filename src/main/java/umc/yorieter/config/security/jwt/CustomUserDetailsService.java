package umc.yorieter.config.security.jwt;

import umc.yorieter.domain.Member;
import umc.yorieter.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.yorieter.repository.MemberRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이 파라미터의 username은 'UserLoginRequestDto에서 변환된 UsernamePasswordAuthenticationToken 객체'에 저장해둔 로그인계정아이디를 의미하며, 이를 비교하는 역할이다.

        return memberRepository.findByUsername(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " 을 DB에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member) {  // username로 Member을 찾고, 찾은 Member의 '사용자DB의PKid,비밀번호,권한'을 가지고 UserDetails 객체를 생성한다.

        // 권한 가져오기
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(member.getId()),  // 헷갈리지말자. 여기서 로그인아이디 대신 사용자DB의PKid를 String자료형으로 변환하여 집어넣는다.
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}