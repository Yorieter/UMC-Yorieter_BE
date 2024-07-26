package umc.yorieter.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@NoArgsConstructor
public class MemberLoginRequestDTO{

    private String username;
    private String password;


    @Builder
    public MemberLoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }


    // UsernamePasswordAuthenticationToken을 반환하여 차후 이 객체를 이용하여 로그인계정아이디와 비밀번호가 일치하는지 검증하는 로직을 사용할 예정임.
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}