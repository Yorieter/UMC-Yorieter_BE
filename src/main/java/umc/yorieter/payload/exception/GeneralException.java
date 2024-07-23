package umc.yorieter.payload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.yorieter.payload.BaseErrorCode;
import umc.yorieter.payload.dto.ErrorReasonDto;
import umc.yorieter.payload.status.ErrorStatus;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode code;

    public GeneralException(ErrorStatus errorStatus, String message) {
        super(errorStatus.getMessage(message));
        this.code = errorStatus;
    }

    public GeneralException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus.getMessage(message), cause);
        this.code = errorStatus;
    }


    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
