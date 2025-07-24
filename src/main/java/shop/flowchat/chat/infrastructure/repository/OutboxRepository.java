package shop.flowchat.chat.infrastructure.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.domain.outbox.EventStatus;
import shop.flowchat.chat.domain.outbox.Outbox;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findByStatusIn(Collection<EventStatus> statuses);
    Optional<Outbox> findByEventId(String eventId);

}
