package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.converter.MemberConverter;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.RecipeLike;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.MemberService.MemberQueryService;
import umc.yorieter.service.MemberService.MemberService;
import umc.yorieter.web.dto.request.MemberRequestDTO;
import umc.yorieter.validation.annotation.CheckPage;
import umc.yorieter.validation.validator.CheckPageValidator;
import umc.yorieter.validation.annotation.ExistMember;
import umc.yorieter.web.dto.response.MemberResponseDTO;


@RestController
@RequiredArgsConstructor
@RequestMapping("/my-page")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;
    private final CheckPageValidator checkPageValidator;

    // 멤버 프로필 조회
    @Operation(summary = "멤버 프로필 조회 API", description = "멤버 프로필을 조회합니다.")
    @GetMapping("/{memberId}")
    public ApiResponse<MemberResponseDTO.MemberDetailDto> getMemberDetail(@PathVariable Long memberId) {
        return ApiResponse.onSuccess(memberService.getMemberDetail(memberId));
    }

    // 내 정보 수정
    @Operation(summary = "멤버 프로필 수정 API", description = "멤버 프로필을 수정합니다.")
    @PatchMapping("/{memberId}")
    public ApiResponse<MemberResponseDTO.MemberDetailDto> updateMember(
            @PathVariable Long memberId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request", required = false) @Valid MemberRequestDTO.MemberUpdateDto request) {
        return ApiResponse.onSuccess(memberService.updateMember(memberId, image, request));
    }

    //내가 작성한 댓글 조회
    @GetMapping("{memberId}/comments")
    @Operation(summary = "내가 작성한 댓글 목록 조회 API", description = "특정 유저가 작성한 댓글들의 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    public ApiResponse<MemberResponseDTO.CommentPreViewListDTO> getCommentListByMemberId(
            @ExistMember @PathVariable(name = "memberId") Long memberId,
            @CheckPage @RequestParam(name = "page") Integer page) {
        Integer validatedPage = checkPageValidator.validateAndTransformPage(page);
        Page<Comment> commentList = memberQueryService.getCommentListByMemberId(memberId, validatedPage);
        return ApiResponse.onSuccess(MemberConverter.commentPreViewListDTO(commentList));
    }

    // 내가 작성한 게시물 조회
    @GetMapping("{memberId}/recipes")
    @Operation(summary = "내가 작성한 레시피 목록 조회 API", description = "특정 유저가 작성한 레시피들의 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    public ApiResponse<MemberResponseDTO.RecipePreViewListDTO> getRecipeListByMember(
            @ExistMember @PathVariable(name = "memberId") Long memberId,
            @CheckPage @RequestParam(name = "page") Integer page) {
        Integer validatedPage = checkPageValidator.validateAndTransformPage(page);
        Page<Recipe> recipeList = memberQueryService.getRecipeListByMemberId(memberId, validatedPage);
        return ApiResponse.onSuccess(MemberConverter.recipePreViewListDTO(recipeList));
    }


    // 내가 좋아요한 게시물 조회
    @GetMapping("{memberId}/recipeLikes")
    @Operation(summary = "내가 좋아요한 레시피 목록 조회 API", description = "특정 유저가 좋아요한 레시피들의 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    public ApiResponse<MemberResponseDTO.RecipeLikePreViewListDTO> getRecipeLikeListByMember(
            @ExistMember @PathVariable(name = "memberId") Long memberId,
            @CheckPage @RequestParam(name = "page") Integer page) {
        Integer validatedPage = checkPageValidator.validateAndTransformPage(page);
        Page<RecipeLike> recipeLikeList = memberQueryService.getRecipeLikeListByMemberId(memberId, validatedPage);
        return ApiResponse.onSuccess(MemberConverter.recipeLikePreViewListDTO(recipeLikeList));
    }
}