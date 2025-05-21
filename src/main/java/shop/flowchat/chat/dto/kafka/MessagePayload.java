package shop.flowchat.chat.dto.kafka;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.dto.common.AttachmentDto;

public record MessagePayload(
    UUID chatId,
    UUID memberId,
    String content,
    List<AttachmentDto> attachments,
    LocalDateTime createdAt,
    String token
) {}
