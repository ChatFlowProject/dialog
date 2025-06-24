package shop.flowchat.chat.dto.kafka;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.dto.common.AttachmentDto;
import shop.flowchat.chat.dto.common.Sender;

public record MessagePayload(
    UUID chatId,
    Sender sender,
    String content,
    List<AttachmentDto> attachments,
    LocalDateTime createdAt
) {}
