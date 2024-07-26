package umc.yorieter.converter;

import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

public class CommentConverter {
    public static CommentResponseDTO toResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .recipeId(comment.getRecipe().getId())
                .memberId(comment.getMember().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }


    public static Comment toEntity(CommentRequestDTO requestDTO, Recipe recipe, Member member) {
        return Comment.builder()
                .content(requestDTO.getContent())
                .recipe(recipe)
                .member(member)
                .build();
    }}
