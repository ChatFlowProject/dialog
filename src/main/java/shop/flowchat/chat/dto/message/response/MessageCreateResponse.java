package shop.flowchat.chat.dto.message.response;

import java.util.List;
import shop.flowchat.chat.dto.common.Attachment;

public record MessageCreateResponse(
        Long chatRoomId,
        Long messageId,
        String senderName,
        String message,
        List<Attachment> attachments,
        String timestamp,
        String status
) {}
