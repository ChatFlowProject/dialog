package shop.flowchat.chat.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.entity.Chat;
import shop.flowchat.chat.entity.ChatType;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
  List<Chat> findByTypeAndMembersMemberId(ChatType type, UUID memberId);
}