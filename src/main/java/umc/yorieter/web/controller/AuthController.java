package umc.yorieter.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.yorieter.domain.Member;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.service.AuthService.AuthService;
import umc.yorieter.web.dto.request.MemberLoginRequestDTO;
import umc.yorieter.web.dto.request.MemberSignupRequestDTO;
import umc.yorieter.web.dto.response.MemberResponseDTO;
import umc.yorieter.web.dto.response.TokenDTO;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 [jwt X]")
    public ApiResponse<MemberResponseDTO> signUp(@RequestBody MemberSignupRequestDTO memberSignupRequestDto) {
        MemberResponseDTO memberResponseDto = authService.signup(memberSignupRequestDto);
        return ApiResponse.onCreate(memberResponseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 [jwt X]")
    public ApiResponse<TokenDTO> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDto) {
        TokenDTO tokenDto = authService.login(memberLoginRequestDto);  // 로그인.

        Member member = memberRepository.findByUsername(memberLoginRequestDto.getUsername()).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        tokenDto.setUsername(member.getUsername());
        tokenDto.setNickname(member.getUsername());

        return ApiResponse.onSuccess(tokenDto);
    }

    @GetMapping("/auth/logout")
    @Operation(summary = "로그아웃 [jwt O]")
    public ApiResponse logout() {  // 로그아웃 시
        return ApiResponse.onSuccess(null);
    }
}
