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
    String description;
    Provider provider;

    @Builder
    public MemberSignupRequestDTO(String username, String password, String nickname, String description, Provider provider) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.description = description;
        this.provider = provider;
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.MemberJoinBuilder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .term1(CHECKED)
                .term2(CHECKED)
                .term3(CHECKED)
                .authority(Authority.ROLE_USER)
                .description(description)
                .provider(provider)
                .build();
    }
}
