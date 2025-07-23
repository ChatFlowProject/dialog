package shop.flowchat.chat.command.dto;

import java.util.List;
import java.util.UUID;

public record CacheRequest(
    UUID memberId,
    List<Channel> channels
) {
    public record Channel(
        Long channelId,
        UUID chatId,
        Long lastMessageId
    ) {}
}
