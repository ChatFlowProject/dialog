package shop.flowchat.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.dto.chat.response.ChatCreateResponse;
import shop.flowchat.chat.dto.common.ApiResponse;
import shop.flowchat.chat.service.ChatService;

@Tag(name = "채팅 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "채팅방 생성")
    @PostMapping()
    public ApiResponse<ChatCreateResponse> createChat() {
        return ApiResponse.success(chatService.createChat());
    }

}
