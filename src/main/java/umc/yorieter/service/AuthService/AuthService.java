package umc.yorieter.service.AuthService;

import umc.yorieter.web.dto.request.MemberLoginRequestDTO;
import umc.yorieter.web.dto.request.MemberSignupRequestDTO;
import umc.yorieter.web.dto.response.MemberResponseDTO;
import umc.yorieter.web.dto.response.TokenDTO;

public interface AuthService {
    MemberResponseDTO signup(MemberSignupRequestDTO memberSignupRequestDto);
    TokenDTO login(MemberLoginRequestDTO memberLoginRequestDto);
}
