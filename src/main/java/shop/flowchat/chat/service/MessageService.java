package shop.flowchat.chat.service;

import feign.FeignException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.chat.client.MemberClient;
import shop.flowchat.chat.dto.common.Direction;
import shop.flowchat.chat.dto.member.request.MemberListRequest;
import shop.flowchat.chat.dto.member.response.MemberResponse;
import shop.flowchat.chat.dto.member.response.MemberSimpleResponse;
import shop.flowchat.chat.dto.message.request.MessageCacheRequest;
import shop.flowchat.chat.dto.message.response.MessageResponse;
import shop.flowchat.chat.entity.Message;
import shop.flowchat.chat.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberClient memberClient;
    private final RedisTemplate<String, Object> redisTemplate;

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

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByDirection(String token, UUID chatId, Long messageId, Direction direction, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Message> messages;

        if (direction == Direction.UP) {
            messages = messageRepository.findByChatIdAndIdLessThanOrderByIdDesc(chatId, messageId, pageRequest);
        } else if (direction == Direction.INCLUSIVE_UP) {
            messages = messageRepository.findByChatIdAndIdLessThanEqualOrderByIdDesc(chatId, messageId, pageRequest);
        } else {
            messages = messageRepository.findByChatIdAndIdGreaterThanOrderByIdAsc(chatId, messageId, pageRequest);
        }

        Map<UUID, MemberSimpleResponse> memberMap = getMemberMap(token, messages);

        return messages.stream()
                .map(message -> {
                    MemberSimpleResponse member = memberMap.get(message.getMemberId());
                    return MessageResponse.from(message, member);
                }).toList();
    }

    private Map<UUID, MemberSimpleResponse> getMemberMap(String token, List<Message> messages) {
        Set<UUID> memberIds = messages.stream()
                .map(Message::getMemberId)
                .collect(Collectors.toSet());

        MemberListRequest request = new MemberListRequest(new ArrayList<>(memberIds));
        MemberResponse memberResponses;
        try {
            memberResponses = memberClient.getMemberInfoList(token, request).data();
        } catch (FeignException e) {
            throw new RuntimeException("회원 정보 조회에 실패했습니다.", e);
        }

        return memberResponses.memberList().stream()
                .collect(Collectors.toMap(MemberSimpleResponse::id, m -> m));
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getLatestMessages(String token, UUID chatId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Message> messages = messageRepository.findByChatIdOrderByIdDesc(chatId, pageRequest);
        Collections.reverse(messages);

        Map<UUID, MemberSimpleResponse> memberMap = getMemberMap(token, messages);

        return messages.stream()
                .map(message -> {
                    MemberSimpleResponse member = memberMap.get(message.getMemberId());
                    return MessageResponse.from(message, member);
                }).toList();
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

            Map<UUID, MemberSimpleResponse> memberMap = getMemberMap(null, messages);

            List<MessageResponse> messageResponses = messages.stream()
                    .map(m -> MessageResponse.from(m, memberMap.get(m.getMemberId())))
                    .toList();

            String key = memberId + ":" + channelId;
            redisTemplate.opsForList().rightPushAll(key, messageResponses);
            redisTemplate.opsForList().trim(key, -30, -1);
        }
    }
}
