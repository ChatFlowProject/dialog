package shop.flowchat.chat.common.exception.custom;

import shop.flowchat.chat.common.exception.ErrorCode;

public class KafkaEventSendException extends ServiceException {

    public KafkaEventSendException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public KafkaEventSendException(String message) {
        super(ErrorCode.EXTERNAL_SERVER_ERROR, message);
    }

    public KafkaEventSendException(ErrorCode errorCode) {
        super(errorCode);
    }

}