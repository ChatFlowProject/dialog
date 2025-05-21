package shop.flowchat.chat.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}