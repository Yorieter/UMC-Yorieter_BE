package umc.yorieter.service.CommentService;

import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    CommentResponseDTO createComment (CommentRequestDTO commentRequestDTO);
    List<CommentResponseDTO> getCommentsByRecipeId (Long recipeId);
    void deleteComment (Long recipeId);
    CommentResponseDTO createReply(Long parentCommentId, CommentRequestDTO commentRequestDTO);
}
