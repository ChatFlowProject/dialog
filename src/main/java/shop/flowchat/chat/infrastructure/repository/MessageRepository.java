package shop.flowchat.chat.infrastructure.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.domain.message.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatIdAndIdLessThanOrderByIdDesc(UUID chatId, Long messageId, PageRequest pageRequest);
    List<Message> findByChatIdAndIdLessThanEqualOrderByIdDesc(UUID chatId, Long messageId, PageRequest pageRequest);
    List<Message> findByChatIdAndIdGreaterThanOrderByIdAsc(UUID chatId, Long messageId, PageRequest pageRequest);
    List<Message> findByChatIdOrderByIdDesc(UUID id, PageRequest pageRequest);
}