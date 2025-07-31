package shop.flowchat.chat.external.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import shop.flowchat.chat.command.service.MessageCommandService;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageDeletePayload;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageUpdatePayload;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageCreatePayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {
    private final ObjectMapper objectMapper;
    private final MessageCommandService commandService;

    @KafkaListener(topics = "chat")
    public void consume(ConsumerRecord<String, String> record, @Header(name = "eventType", required = false) String eventType) {
        try {
            if (eventType == null) {
                log.warn("eventType Header is null. Skipping record: {}", record);
                return;
            }

            switch (eventType) {
                case "messageCreate" -> {
                    MessageCreatePayload response = objectMapper.readValue(record.value(), MessageCreatePayload.class);
                    commandService.sendMessage(response.chatId(), response);
                }
                case "messageUpdate" -> {
                    MessageUpdatePayload response = objectMapper.readValue(record.value(), MessageUpdatePayload.class);
                    commandService.sendMessage(response.chatId(), response);
                }
                case "messageDelete" -> {
                    MessageDeletePayload response = objectMapper.readValue(record.value(), MessageDeletePayload.class);
                    commandService.sendMessage(response.chatId(), response);
                }
            }

        } catch (Exception e) {
            log.error("❌ Kafka 메시지 처리 중 오류 발생", e);
        }
    }
}
