package shop.flowchat.chat.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    BAD_REQUEST(400, 40001, "파라미터 오류"),
    ENTITY_NOT_FOUND(404, 40401, "리소스를 찾을 수 없음"),
    INTERNAL_SERVER_ERROR(500, 50001, "서버 에러"),
    ;

    private final int httpStatus;
    private final int errorCode;
    private final String message;

    ErrorCode(int httpStatus, int errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}