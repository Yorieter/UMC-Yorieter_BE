package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.CommentService.CommentService;
import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes/{recipeId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성")
    public ApiResponse<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO commentRequestDTO, @PathVariable Long recipeId) {
//        commentRequestDTO.setRecipeId(recipeId);

        CommentResponseDTO commentResponseDTO = commentService.createComment(commentRequestDTO);
        return ApiResponse.onCreate(commentResponseDTO);
    }

//    @GetMapping
//    @Operation(summary = "레시피의 댓글 조회")
//    public ApiResponse<RecipeCommentsResponseDTO> getComments(@PathVariable Long recipeId) {
//        RecipeCommentsResponseDTO responseDTO = commentService.getCommentsByRecipeId(recipeId);
//        return ApiResponse.<RecipeCommentsResponseDTO>builder()
//                .isSuccess(true)
//                .code("COMMENT200")
//                .message("댓글 조회에 성공하였습니다.")
//                .result(responseDTO)
//                .build();
//    }
}

