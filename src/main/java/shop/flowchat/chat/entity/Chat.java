package shop.flowchat.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.flowchat.chat.dto.kafka.MessagePayload;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime createAt;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

    @Builder
    public Chat(LocalDateTime createAt) {
        this.createAt = createAt != null ? createAt : LocalDateTime.now();
    }

    public static Chat create() {
        return Chat.builder()
                .createAt(LocalDateTime.now())
                .build();
    }

}
