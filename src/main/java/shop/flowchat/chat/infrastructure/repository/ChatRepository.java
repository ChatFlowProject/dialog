package shop.flowchat.chat.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}