package umc.yorieter.config.security.util;

import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() { }

    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new GeneralException(ErrorStatus.NOT_FOUND_CONTEXT, "Security Context에 인증 정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName());  // 여기에 getName의 Name은 로그인계정아이디가 아닌, 사용자DB의PKid를 String형식으로 넣어뒀던것이다.
    }
}