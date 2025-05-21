package shop.flowchat.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.flowchat.chat.dto.kafka.MessagePayload;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isUpdated;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "message", orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Builder
    public Message(UUID memberId, Chat chat, String content, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.chat = chat;
        this.content = content;
        this.createdAt = createdAt;
        this.isUpdated = false;
        this.isDeleted = false;
    }

    public static Message create(MessagePayload payload, Chat chat) {
        return Message.builder()
                .memberId(payload.memberId())
                .chat(chat)
                .content(payload.content())
                .createdAt(payload.createdAt())
                .build();
    }

    public void setIsDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        this.isUpdated = true;
    }
}
