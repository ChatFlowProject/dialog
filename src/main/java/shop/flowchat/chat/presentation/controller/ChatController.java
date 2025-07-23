package shop.flowchat.chat.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.presentation.dto.response.ChatCreateResponse;
import shop.flowchat.chat.common.dto.response.ApiResponse;
import shop.flowchat.chat.command.service.ChatCommandService;

@Tag(name = "채팅 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatCommandService chatCommandService;

    @Operation(summary = "채팅방 생성")
    @PostMapping()
    public ApiResponse<ChatCreateResponse> createChat() {
        return ApiResponse.success(chatCommandService.createChat());
    }

}
