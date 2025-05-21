package shop.flowchat.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}