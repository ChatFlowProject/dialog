package shop.flowchat.chat.dto.message.request;

import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.dto.common.AttachmentDto;

public record MessageCreateRequest(
        String content,
        List<AttachmentDto> attachments,
        List<String> memberIds
) { }