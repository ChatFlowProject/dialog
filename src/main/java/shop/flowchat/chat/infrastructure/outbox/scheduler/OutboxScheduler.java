package shop.flowchat.chat.infrastructure.outbox.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import shop.flowchat.chat.domain.outbox.EventStatus;
import shop.flowchat.chat.domain.outbox.Outbox;
import shop.flowchat.chat.infrastructure.outbox.producer.KafkaEventProducer;
import shop.flowchat.chat.infrastructure.repository.OutboxRepository;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxRepository outboxRepository;
    private final KafkaEventProducer kafkaEventPublisher;

    @Scheduled(fixedDelay = 600000) // 10분 간격
    @Transactional
    public void retryPendingMessages() {
        List<Outbox> eventList = outboxRepository.findByStatusIn(List.of(EventStatus.PENDING, EventStatus.FAILED));
        for (Outbox outbox : eventList) {
            try {
                kafkaEventPublisher.sendEvent(
                        outbox.getAggregateType(),   // topic
                        outbox.getEventId(),         // eventId (header & Outbox Unique key)
                        outbox.getEventType(),       // eventType (header)
                        outbox.getAggregateId(),     // record key
                        outbox.getPayload()          // record message
                );
                outbox.markSuccess();
            } catch (Exception e) {
                outbox.markFailed();
            }
            outboxRepository.save(outbox);
        }
    }

}
