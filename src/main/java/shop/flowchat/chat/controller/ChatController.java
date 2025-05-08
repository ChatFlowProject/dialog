package shop.flowchat.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.dto.chat.response.DmListResponse;
import shop.flowchat.chat.dto.common.ApiResponse;
import shop.flowchat.chat.service.ChatService;

@Tag(name = "채팅 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "DM 리스트 조회")
    @GetMapping("/dm")
    public ApiResponse<List<DmListResponse>> getDmList(@RequestHeader("Authorization") String token) {
        return ApiResponse.success(chatService.getChatsByType(token));
    }
    // Todo :  채팅방 생성, 채팅 메시지 조회, 채팅 검색
}
