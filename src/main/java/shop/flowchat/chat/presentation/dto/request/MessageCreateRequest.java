package shop.flowchat.chat.presentation.dto.request;

import java.util.List;
import shop.flowchat.chat.common.dto.value.AttachmentDto;

public record MessageCreateRequest(
        String content,
        List<AttachmentDto> attachments,
        List<String> memberIds
) { }