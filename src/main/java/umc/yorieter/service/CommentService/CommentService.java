package umc.yorieter.service.CommentService;

import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    CommentResponseDTO createComment (Long recipeId, CommentRequestDTO commentRequestDTO);
    List<CommentResponseDTO> getCommentsByRecipeId (Long recipeId);
    void deleteComment (Long recipeId);
    CommentResponseDTO createReply(Long parentCommentId, Long recipeId, CommentRequestDTO commentRequestDTO);
    Long getCommentOwnerId(Long commentId);

}
