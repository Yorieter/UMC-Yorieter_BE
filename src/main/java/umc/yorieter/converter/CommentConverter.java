package umc.yorieter.converter;

import org.springframework.stereotype.Component;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

import java.time.format.DateTimeFormatter;

@Component
public class CommentConverter {
    public static CommentResponseDTO toCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .recipeId(comment.getRecipe().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
