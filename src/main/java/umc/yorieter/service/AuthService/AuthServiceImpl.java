package umc.yorieter.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umc.yorieter.config.security.jwt.TokenProvider;
import umc.yorieter.domain.Member;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.web.dto.request.MemberLoginRequestDTO;
import umc.yorieter.web.dto.request.MemberSignupRequestDTO;
import umc.yorieter.web.dto.response.MemberResponseDTO;
import umc.yorieter.web.dto.response.TokenDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Transactional
    @Override
    public MemberResponseDTO signup(MemberSignupRequestDTO memberSignupRequestDto) {

        String username = memberSignupRequestDto.getUsername();
        memberRepository.findByUsername(username)
                .ifPresent(member -> {  // 해당 로그인아이디의 사용자가 이미 존재한다면,
                    String errorMessage = ("중복된 아이디 '" + username + "'를 입력했습니다.");
                    throw new GeneralException(ErrorStatus.USERNAME_ALREADY_EXISTS, errorMessage);
                });

        Member member = memberSignupRequestDto.toEntity(passwordEncoder);

        // 기본 프로필 이미지 설정
        String defaultProfileImageUrl = "https://umc-yorieter.s3.ap-northeast-2.amazonaws.com/default.png"; // 실제 경로로 대체
        member.updateProfileUrl(defaultProfileImageUrl);

        // Member와 MemberProfile 저장
        memberRepository.save(member);

        return new MemberResponseDTO(member);
    }

    @Transactional
    @Override
    public TokenDTO login(MemberLoginRequestDTO memberLoginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = memberLoginRequestDto.toAuthentication();

        // 여기서 실제로 검증이 이루어진다.
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }
}