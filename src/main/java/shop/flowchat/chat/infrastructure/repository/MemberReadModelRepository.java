package shop.flowchat.chat.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.chat.domain.member.MemberReadModel;

public interface MemberReadModelRepository extends JpaRepository<MemberReadModel, UUID> {

}