package shop.flowchat.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.flowchat.chat.dto.common.AttachmentDto;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    @Enumerated(EnumType.STRING)
    private AttachmentType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Builder
    public Attachment(Message message, String url, AttachmentType type) {
        this.message = message;
        this.url = url;
        this.type = type;
    }

    public static Attachment create(Message message, AttachmentDto attachmentDto) {
        return Attachment.builder()
                .message(message)
                .url(attachmentDto.url())
                .type(attachmentDto.type())
                .build();
    }
}
