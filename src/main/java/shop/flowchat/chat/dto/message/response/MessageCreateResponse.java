package shop.flowchat.chat.dto.message.response;

import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.dto.common.Attachment;

public record MessageCreateResponse(
        UUID chatRoomId,
        UUID messageId,
        String senderName,
        String message,
        List<Attachment> attachments,
        String timestamp,
        String status
) {}
