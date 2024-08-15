package umc.yorieter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.yorieter.domain.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Long memberId;
    private Long recipeId;
    private Long parentCommentId;
    private List<CommentResponseDTO> childComments;

    public static CommentResponseDTO fromComment(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .recipeId(comment.getRecipe().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentCommentId(comment.getParentComment() == null ? null : comment.getParentComment().getId())  // Set parentCommentId
                .childComments(comment.getChildComments() == null ?
                        new ArrayList<>() :
                        comment.getChildComments().stream()
                                .map(CommentResponseDTO::fromComment)
                                .collect(Collectors.toList()))
                .build();
    }
}
