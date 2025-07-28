package shop.flowchat.chat.query;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.flowchat.chat.domain.member.MemberReadModel;
import shop.flowchat.chat.common.dto.value.Direction;
import shop.flowchat.chat.common.dto.response.MessageResponse;
import shop.flowchat.chat.domain.message.Message;
import shop.flowchat.chat.infrastructure.repository.MemberReadModelRepository;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageQueryService {

    private final MessageRepository messageRepository;
    private final MemberReadModelRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByDirection(UUID chatId, Long messageId, Direction direction, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Message> messages;

        if (direction == Direction.UP) {
            messages = messageRepository.findByChatIdAndIdLessThanOrderByIdDesc(chatId, messageId, pageRequest);
        } else if (direction == Direction.INCLUSIVE_UP) {
            messages = messageRepository.findByChatIdAndIdLessThanEqualOrderByIdDesc(chatId, messageId, pageRequest);
        } else {
            messages = messageRepository.findByChatIdAndIdGreaterThanOrderByIdAsc(chatId, messageId, pageRequest);
        }

        return getMessageResponses(messages);
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getLatestMessages(UUID chatId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Message> messages = messageRepository.findByChatIdOrderByIdDesc(chatId, pageRequest);
        Collections.reverse(messages);

        return getMessageResponses(messages);
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