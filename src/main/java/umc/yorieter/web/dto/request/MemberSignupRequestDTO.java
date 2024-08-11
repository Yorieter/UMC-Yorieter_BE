package umc.yorieter.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.yorieter.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import umc.yorieter.domain.enums.Authority;
import umc.yorieter.domain.enums.Provider;

import static umc.yorieter.domain.enums.Term.CHECKED;

@Getter
@NoArgsConstructor
public class MemberSignupRequestDTO {

    String username;
    String password;
    String nickname;

    @Builder
    public MemberSignupRequestDTO(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.MemberJoinBuilder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .authority(Authority.ROLE_USER)
                .term1(CHECKED)
                .term2(CHECKED)
                .term3(CHECKED)
                .description("건강한 재료로 제한없이 먹고 싶은 것 모두 요리합니다 :)")
                .build();
    }
}
