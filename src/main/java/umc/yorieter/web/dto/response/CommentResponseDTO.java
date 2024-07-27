package umc.yorieter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.yorieter.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberId;
    private Long recipeId;

    public static CommentResponseDTO fromComment(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .recipeId(comment.getRecipe().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
