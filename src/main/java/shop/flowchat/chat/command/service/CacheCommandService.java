package shop.flowchat.chat.command.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shop.flowchat.chat.command.dto.CacheRequest;
import shop.flowchat.chat.common.dto.response.MessageResponse;
import shop.flowchat.chat.domain.message.Message;
import shop.flowchat.chat.domain.readmodel.MemberReadModel;
import shop.flowchat.chat.infrastructure.repository.MemberReadModelRepository;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class CacheCommandService {
    private final MessageRepository messageRepository;
    private final MemberReadModelRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void cacheMessagesByUser(CacheRequest request) {
        UUID memberId = request.memberId();

        for (CacheRequest.Channel channel : request.channels()) {
            Long channelId = channel.channelId();
            UUID chatId = channel.chatId();
            Long lastMessageId = channel.lastMessageId();

            PageRequest pageRequest = PageRequest.of(0, 30);
            List<Message> messages = messageRepository.findByChatIdAndIdLessThanOrderByIdDesc(chatId, lastMessageId, pageRequest);
            Collections.reverse(messages);

            List<MessageResponse> messageResponses = getMessageResponses(messages);

            String key = memberId + ":" + channelId;
            redisTemplate.opsForList().rightPushAll(key, messageResponses);
            redisTemplate.opsForList().trim(key, -30, -1);
        }
    }

    private List<MessageResponse> getMessageResponses(List<Message> messages) {
        Set<UUID> memberIds = messages.stream()
                .map(Message::getMemberId)
                .collect(Collectors.toSet());

        List<MemberReadModel> members = memberRepository.findAllById(memberIds);

        Map<UUID, MemberReadModel> memberMap = members.stream()
                .collect(Collectors.toMap(MemberReadModel::getId, member -> member));

        return messages.stream()
                .map(message -> MessageResponse.from(message, memberMap.get(message.getMemberId())))
                .toList();
    }
}
