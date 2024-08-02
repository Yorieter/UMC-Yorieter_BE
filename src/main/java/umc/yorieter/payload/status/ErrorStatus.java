package umc.yorieter.payload.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import umc.yorieter.payload.BaseErrorCode;
import umc.yorieter.payload.dto.ErrorReasonDto;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "Bad request"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON4001",  "Validation error"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON4002", "Requested resource not found"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000",  "Internal error"),
    DATA_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5001",  "Data access error"),


    // Token Error
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"TOKEN4000", "Invalid token"),

    // Member_Error
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4000", "MEMBER not found"),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER4001", "USERNAME already exists"),
    NO_EDIT_DELETE_PERMISSION(HttpStatus.BAD_REQUEST, "MEMBER4002", "수정/삭제의 권한이 없습니다."),

    // 멤버 관련 응답
    MEMBER_NOT_EXIST_ERROR(HttpStatus.resolve(400),"MEMBER400","존재하지 않는 회원입니다."),

    // Security Error
    NOT_FOUND_CONTEXT(HttpStatus.NOT_FOUND,"Security4000", "SecurityContext not found"),



    // 레시피 관련 응답
    RECIPE_NOT_EXIST_ERROR(HttpStatus.resolve(400),"RECIPE400","존재하지 않는 레시피입니다."),
    CANNOT_LIKE_OWN_RECIPE_ERROR(HttpStatus.resolve(400),"MYRECIPE400","내가 작성한 레시피는 좋아요 누를 수 없습니다."),
    RECIPE_LIKE_ALREADY_ERROR(HttpStatus.resolve(400),"RECIPE409","이미 좋아요 한 레시피입니다."),
    RECIPELIKE_NOT_EXIST_ERROR(HttpStatus.resolve(400),"RECIPELIKE400","좋아요 한 레시피가 아닙니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
