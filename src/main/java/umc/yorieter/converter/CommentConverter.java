package umc.yorieter.converter;

import org.springframework.stereotype.Component;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CommentConverter {
    public static CommentResponseDTO toCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .recipeId(comment.getRecipe().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentCommentId(comment.getParentComment() == null ? null : comment.getParentComment().getId())  // Set parentCommentId
                .childComments(comment.getChildComments().stream()
                        .map(CommentConverter::toCommentResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public Comment toCommentEntity(CommentRequestDTO dto, Recipe recipe, Member member, Comment parentComment) {
        return Comment.builder()
                .content(dto.getContent())
                .recipe(recipe)
                .member(member)
                .parentComment(parentComment)
                .childComments(new ArrayList<>())
                .build();
    }

}
