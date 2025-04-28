package shop.flowchat.chat.dto.message.response;

import java.util.List;
import shop.flowchat.chat.dto.common.Sender;
import shop.flowchat.chat.dto.common.Attachment;
import shop.flowchat.chat.dto.message.request.MessageCreateRequest;

public record MessagePushResponse(
        Long chatRoomId,
        Long messageId,
        Sender sender,
        String message,
        List<Attachment> attachments,
        String timestamp,
        String status
) {
    // static of() 메서드 추가
    public static MessagePushResponse of(Long chatRoomId, MessageCreateRequest request) {
        return new MessagePushResponse(
                chatRoomId,
                null,
                new Sender(1L, "승은", "https://example.com/avatar.png"),
                request.message(),
                request.attachments(),
                java.time.LocalDateTime.now().toString(),
                "sent"
        );
    }
}