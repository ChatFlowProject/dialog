package shop.flowchat.chat.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.chat.presentation.dto.response.ChatCreateResponse;
import shop.flowchat.chat.domain.Chat;
import shop.flowchat.chat.infrastructure.repository.ChatRepository;

@Service
@RequiredArgsConstructor
public class ChatCommandService {
    private final ChatRepository chatRepository;

    @Transactional
    public ChatCreateResponse createChat() {
        Chat chat = Chat.builder().build();
        chatRepository.save(chat);
        return new ChatCreateResponse(chat.getId());
    }
}
