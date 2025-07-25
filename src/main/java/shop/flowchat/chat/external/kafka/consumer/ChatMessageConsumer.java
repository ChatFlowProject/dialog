package shop.flowchat.chat.external.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import shop.flowchat.chat.command.service.MessageCommandService;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageEventPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {
    private final ObjectMapper objectMapper;
    private final MessageCommandService commandService;

    @KafkaListener(topics = "chat")
    public void consume(String rawMessage) {
        try {
            MessageEventPayload payload = objectMapper.readValue(rawMessage, MessageEventPayload.class);
            log.info("Kafka 메시지 수신 및 역직렬화 완료: {}", payload);

            commandService.createMessage(payload);

        } catch (Exception e) {
            log.error("❌ Kafka 메시지 처리 중 오류 발생", e);
        }
    }
}
