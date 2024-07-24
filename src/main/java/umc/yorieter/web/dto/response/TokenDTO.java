package umc.yorieter.web.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {  // jwt Token ResponseDto (+ login username)

    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;

    @Setter
    private String username;
    @Setter
    private String email;
}