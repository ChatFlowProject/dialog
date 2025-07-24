package shop.flowchat.chat.domain.readmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import shop.flowchat.chat.external.kafka.dto.MemberEventPayload;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberReadModel {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String nickname;
    private String name;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private MemberReadModelState state;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private MemberReadModel(UUID id, String nickname, String name, String avatarUrl, MemberReadModelState state) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.state = state;
    }

    public static MemberReadModel create(MemberEventPayload payload) {
        return MemberReadModel.builder()
                .id(payload.id())
                .nickname(payload.nickname())
                .name(payload.name())
                .avatarUrl(payload.avatarUrl())
                .state(payload.state())
                .build();
    }

    public boolean isNewProfileUpdateEvent(LocalDateTime timestamp) {
        return updatedAt == null || timestamp.isAfter(updatedAt);
    }

    public void updateProfile(MemberEventPayload payload) {
        this.nickname = payload.nickname();
        this.name = payload.name();
        this.avatarUrl = payload.avatarUrl();
        this.updatedAt = payload.timestamp();
    }

}
