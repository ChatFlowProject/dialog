package shop.flowchat.chat.dto.message.request;

import java.util.List;
import java.util.UUID;

public record MessageCacheRequest(
    UUID memberId,
    List<Channel> channels
) {
    public record Channel(
        Long channelId,
        UUID chatId,
        Long lastMessageId
    ) {}
}
