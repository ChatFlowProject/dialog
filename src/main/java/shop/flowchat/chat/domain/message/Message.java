package shop.flowchat.chat.domain.message;

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
import org.springframework.data.annotation.LastModifiedDate;
import shop.flowchat.chat.domain.chat.Chat;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;

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

    private String content;

    private UUID invitedTeamId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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
    public Message(UUID memberId, Chat chat, String content, UUID invitedTeamId, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.chat = chat;
        this.content = content;
        this.invitedTeamId = invitedTeamId;
        this.createdAt = createdAt;
        this.isUpdated = false;
        this.isDeleted = false;
    }

    public static Message create(MessageCreateRequest request, Chat chat, UUID memberId) {
        return Message.builder()
                .memberId(memberId)
                .chat(chat)
                .content(request.content())
                .invitedTeamId(request.invitedTeamId())
                .createdAt(LocalDateTime.now())
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
