package shop.flowchat.chat.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.chat.domain.readmodel.MemberReadModel;
import shop.flowchat.chat.external.kafka.dto.MemberEventPayload;
import shop.flowchat.chat.infrastructure.repository.MemberReadModelRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReadModelCommandService {

    private final MemberReadModelRepository repository;

    public void create(MemberEventPayload payload) {
        if (repository.existsById(payload.id()))
            return;
        repository.save(MemberReadModel.create(payload));
    }

    public void updateProfile(MemberEventPayload payload) { // upsert
        repository.findById(payload.id())
                .ifPresentOrElse(
                        existingMember -> {
                            if (existingMember.isNewProfileUpdateEvent(payload.timestamp())) {
                                existingMember.updateProfile(payload);
                            }
                        },
                        () -> {
                            MemberReadModel memberReadModel = MemberReadModel.create(payload);
                            memberReadModel.updateProfile(payload);
                            repository.save(memberReadModel);
                        }
                );
    }

    public void delete(MemberEventPayload payload) {
        repository.deleteById(payload.id());
    }
}