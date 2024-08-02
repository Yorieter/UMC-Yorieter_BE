package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.MemberService.MemberService;
import umc.yorieter.web.dto.request.MemberRequestDTO;
import umc.yorieter.web.dto.response.MemberResponseDTO;


@RestController
@RequiredArgsConstructor
@RequestMapping("/my-page")
@Slf4j
public class MemberController {

    private final MemberService memberService;

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
}