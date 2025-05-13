package shop.flowchat.chat.dto.kafka;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.dto.common.Attachment;

public record MessagePayload(
    UUID chatId,
    UUID senderId,
    String content,
    List<Attachment> attachments,
    LocalDateTime createdAt,
    Boolean edited,
    String token
) {}
