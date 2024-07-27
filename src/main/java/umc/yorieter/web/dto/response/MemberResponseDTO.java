package umc.yorieter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.yorieter.domain.Member;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberResponseDTO {

    private Long id;
    private String username;


    public MemberResponseDTO(Member entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDetailDto {

        Long id;
        String nickname;
        String description;
        String profileUrl;
    }
}
