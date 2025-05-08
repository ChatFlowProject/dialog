package shop.flowchat.chat.dto.chat.response;

import java.util.List;
import java.util.UUID;

public record DmListResponse(
    UUID chatId,
    List<MemberInfo> members
) {
    public record MemberInfo(
        UUID id,
        String name,
        String avatarUrl
    ) {}
}
