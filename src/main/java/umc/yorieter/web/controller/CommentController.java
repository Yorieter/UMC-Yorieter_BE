package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.CommentService.CommentService;
import umc.yorieter.service.RecipeService.RecipeService;
import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
//    private final RecipeService recipeService;
//    private final MemberService

//    @PostMapping
//    @Operation(summary = "댓글 작성")
//    public ApiResponse<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO requestDTO) {
//
//        CommentResponseDTO commentResponseDTO = commentService.createComment(requestDTO);
//        return ApiResponse.onCreate(commentResponseDTO);
//    }


}

