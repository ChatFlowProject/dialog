package shop.flowchat.chat.infrastructure.outbox.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import shop.flowchat.chat.domain.outbox.EventStatus;
import shop.flowchat.chat.infrastructure.repository.OutboxRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatInternalKafkaListener {
    private final OutboxRepository outboxRepository;

    @KafkaListener(topics = "chat", groupId = "chat-international-group")
    public void consumeChatEvents(String message, @Header("eventId") String eventId) {
        outboxRepository.findByEventId(eventId).ifPresent(outbox -> {
            if (outbox.getStatus() != EventStatus.SUCCESS) {
                outbox.markSuccess();
                outboxRepository.save(outbox);
            }
        });
    }

    @KafkaListener(topics = "mention", groupId = "chat-international-group")
    public void consumeMentionEvents(String message, @Header("eventId") String eventId) {
        outboxRepository.findByEventId(eventId).ifPresent(outbox -> {
            if (outbox.getStatus() != EventStatus.SUCCESS) {
                outbox.markSuccess();
                outboxRepository.save(outbox);
            }
        });
    }

}
