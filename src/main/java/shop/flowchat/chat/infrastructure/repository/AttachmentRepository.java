package shop.flowchat.chat.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.domain.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}