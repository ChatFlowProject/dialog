package shop.flowchat.chat.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import shop.flowchat.chat.dto.kafka.MessagePayload;

@Service
@RequiredArgsConstructor
public class ChatMessageProducer {
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;
    private static final String TOPIC = "chat-message";

    public void sendMessage(MessagePayload payload) {
        kafkaTemplate.send(TOPIC, payload.chatId().toString(), payload);
    }
}
