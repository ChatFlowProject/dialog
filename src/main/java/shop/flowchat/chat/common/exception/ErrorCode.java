package shop.flowchat.chat.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    BAD_REQUEST(400, "Bad Request"),
    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    INVALID_TYPE_VALUE(400, "Invalid Type Value"),
    ACCESS_DENIED(403, "해당 리소스에 접근할 수 있는 권한이 부족합니다."),
    ENTITY_NOT_FOUND(404, "Entity Not Found"),
    RESOURCE_NOT_FOUND(404, "Resource not found"),
    METHOD_NOT_ALLOWED(405, "Invalid Method"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    EXTERNAL_SERVER_ERROR(502, "Bad Gateway"),
    ;

    private final int status;
    private final String message;

    ErrorCode(int httpStatus, String message) {
        this.status = httpStatus;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}