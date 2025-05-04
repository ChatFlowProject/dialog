package shop.flowchat.chat.entity;
import lombok.Builder;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import shop.flowchat.chat.dto.kafka.MessagePayload;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID senderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private String content;
    private LocalDateTime createdAt;
    private Boolean isUpdated;

    @Builder
    public Message(UUID senderId, Chat chat, String content, LocalDateTime createdAt, Boolean isUpdated) {
        this.senderId = senderId;
        this.chat = chat;
        this.content = content;
        this.createdAt = createdAt;
        this.isUpdated = isUpdated;
    }

    public static Message create(MessagePayload payload, Chat chat) {
        return Message.builder()
                .senderId(payload.senderId())
                .chat(chat)
                .content(payload.content())
                .createdAt(payload.createdAt())
                .isUpdated(payload.edited())
                .build();
    }
}
