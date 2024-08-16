package umc.yorieter.converter;

import org.springframework.data.domain.Page;
import umc.yorieter.domain.Comment;
import umc.yorieter.web.dto.response.RecipeResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeConverter {

    public static RecipeResponseDTO.CommentPreViewDTO commentPreViewDTO(Comment comment){
        return RecipeResponseDTO.CommentPreViewDTO.builder()
                .username(comment.getMember().getUsername())
                .content(comment.getContent())
                .commentId((comment.getId()))
                .createdAt(comment.getCreatedAt().toLocalDate())
                .build();
    }

    public static RecipeResponseDTO.CommentPreViewListDTO commentPreViewListDTO(Page<Comment> commentList){
        List<RecipeResponseDTO.CommentPreViewDTO> commentPreViewDTOList = commentList.stream()
                .map(RecipeConverter::commentPreViewDTO).collect(Collectors.toList());

        return RecipeResponseDTO.CommentPreViewListDTO.builder()
                .isLast(commentList.isLast())
                .isFirst(commentList.isFirst())
                .totalPage(commentList.getTotalPages())
                .totalElements(commentList.getTotalElements())
                .listSize(commentPreViewDTOList.size())
                .commentList(commentPreViewDTOList)
                .build();
    }
}
