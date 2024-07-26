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

    @Builder
    public MemberSignupRequestDTO(String username, String password, String nickname, String description, Provider provider) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.description = description;
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
                .description(description)
                .build();
    }
}
