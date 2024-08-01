package umc.yorieter.web.dto.request;

import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    public static class MemberUpdateDto {
        private String nickname;
        private String description;
    }
}
