package shop.flowchat.chat.presentation.dto.request;

import java.util.List;

public record MessageUpdateRequest(
        Long messageId,
        String newContent,
        List<String> memberIds
) {}