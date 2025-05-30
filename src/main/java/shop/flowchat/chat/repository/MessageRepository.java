package shop.flowchat.chat.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatIdAndIdLessThanOrderByIdDesc(UUID chatId, Long lastMessageId, PageRequest pageRequest);
}