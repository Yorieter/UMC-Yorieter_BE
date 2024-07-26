package umc.yorieter.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;

@Getter
@Setter
public class CommentRequestDTO {
    private String content;
    private Long recipeId;
    private Long memberId;
}
