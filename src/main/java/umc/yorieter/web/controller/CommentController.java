package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.CommentService.CommentService;
import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/recipes/{recipeId}/comments")
    @Operation(summary = "댓글 작성 API")
    public ApiResponse<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO commentRequestDTO, @PathVariable Long recipeId) {
        CommentResponseDTO commentResponseDTO = commentService.createComment(recipeId,commentRequestDTO);
        return new ApiResponse<>(true, "COMMENT200","댓글 작성에 성공하였습니다.",commentResponseDTO);
    }

    @GetMapping("recipes/{recipeId}/comments")
    @Operation(summary = "레시피의 댓글 조회 API")
    public ApiResponse<List<CommentResponseDTO>> getComments(@PathVariable Long recipeId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByRecipeId(recipeId);
        return new ApiResponse<>(true, "COMMENT200", "댓글 조회에 성공하였습니다.", comments);
    }

    @DeleteMapping("/recipes/{recipeId}/comments/{commentId}")
    @Operation(summary = "댓글 삭제 API")
    public ApiResponse<String> deleteComment(@PathVariable Long recipeId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ApiResponse<>(true, "COMMENT200", "댓글 삭제에 성공하였습니다.", null);    }

    @PostMapping("/recipes/{recipeId}/comments/{commentId}/replies")
    @Operation(summary = "대댓글 작성 API")
    public ApiResponse<CommentResponseDTO> createReply(
            @RequestBody CommentRequestDTO commentRequestDTO,
            @PathVariable Long recipeId,
            @PathVariable Long commentId) {

        CommentResponseDTO commentResponseDTO = commentService.createReply(commentId,recipeId, commentRequestDTO);

        return new ApiResponse<>(true, "COMMENT200", "대댓글 작성에 성공하였습니다.", commentResponseDTO);
    }
}