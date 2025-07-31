package shop.flowchat.chat.external.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import shop.flowchat.chat.command.service.MemberReadModelCommandService;
import shop.flowchat.chat.external.kafka.dto.MemberEventPayload;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventConsumer {
    private final MemberReadModelCommandService commandService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "member", groupId = "dialog-service-group")
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            // Kafka 메시지 역직렬화
            MemberEventPayload payload = objectMapper.readValue(record.value(), MemberEventPayload.class);

            if (eventType == null || eventType.equals("memberModifyStatus")) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }

            switch (eventType) {
                case "signUp" -> commandService.create(payload);
                case "memberUpdate" -> commandService.updateProfile(payload);
                case "memberDelete" -> commandService.delete(payload);
                default -> log.warn("Unknown member eventType: {} Skipping record: {}", eventType, record);
            }

        } catch (Exception e) {
            // 예외 발생시 offset commit되지 않음 -> 따라서 위의 이벤트 처리 로직은 데이터를 멱등하게 처리해야 함
            log.error("Failed to consume event", e);
        }
    }

}