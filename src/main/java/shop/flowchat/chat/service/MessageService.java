package shop.flowchat.chat.service;

import java.util.List;
import java.util.UUID;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.chat.dto.message.request.MessageCacheRequest;
import shop.flowchat.chat.dto.message.response.MessageCacheResponse;
import shop.flowchat.chat.entity.Message;
import shop.flowchat.chat.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String MESSAGE_CACHE_KEY_PREFIX = "chat:";

    @Transactional
    public void deleteMessage(Long messageId, UUID memberId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

        if (!message.getMemberId().equals(memberId)) {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }

        message.setIsDeleted(true);
        messageRepository.save(message);
    }

    @Transactional
    public void updateMessage(Long messageId, UUID memberId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

        if (!message.getMemberId().equals(memberId)) {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }

        message.updateContent(newContent);
        messageRepository.save(message);
    }

    private String buildKey(UUID memberId, Long channelId) {
        return MESSAGE_CACHE_KEY_PREFIX + memberId + ":" + channelId;
    }

    public void cacheMessagesByUser(MessageCacheRequest request) {
        UUID memberId = request.memberId();

        for (MessageCacheRequest.Channel channel : request.channels()) {
            Long channelId = channel.channelId();
            UUID chatId = channel.chatId();
            Long lastMessageId = channel.lastMessageId();

            PageRequest pageRequest = PageRequest.of(0, 30);
            List<Message> messages = messageRepository.findByChatIdAndIdLessThanOrderByIdDesc(chatId, lastMessageId, pageRequest);
            Collections.reverse(messages);

            List<MessageCacheResponse> messageResponses = messages.stream()
                    .map(MessageCacheResponse::from)
                    .collect(Collectors.toList());

            String key = buildKey(memberId, channelId);
            redisTemplate.opsForList().rightPushAll(key, messageResponses);
            redisTemplate.opsForList().trim(key, -30, -1);
        }
    }
}
