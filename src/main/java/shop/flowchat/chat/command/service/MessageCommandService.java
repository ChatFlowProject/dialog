package shop.flowchat.chat.command.service;

import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.flowchat.chat.domain.message.Message;
import shop.flowchat.chat.external.kafka.dto.MessagePayload;
import shop.flowchat.chat.infrastructure.outbox.event.message.MentionCreateEvent;
import shop.flowchat.chat.infrastructure.outbox.event.message.MessageCreateEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MentionCreatePayload;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;
import shop.flowchat.chat.infrastructure.security.StompPrincipal;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;

@Service
@RequiredArgsConstructor
public class MessageCommandService {
    private final ApplicationEventPublisher eventPublisher;
    private final MessageRepository messageRepository;

    @Transactional
    public void sendMessage(UUID chatId, MessageCreateRequest request, StompPrincipal principal) {
        LocalDateTime now = LocalDateTime.now();

        eventPublisher.publishEvent(new MessageCreateEvent(chatId.toString(), MessagePayload.from(chatId, request, principal, now)));
        if (request.memberIds() != null && !request.memberIds().isEmpty()) {
            eventPublisher.publishEvent(new MentionCreateEvent(chatId.toString(), MentionCreatePayload.from(chatId, request, principal, now)));
        }
    }

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
}