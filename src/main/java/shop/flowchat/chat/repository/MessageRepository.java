package shop.flowchat.chat.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}