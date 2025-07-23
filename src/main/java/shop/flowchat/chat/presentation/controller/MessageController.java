package shop.flowchat.chat.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.command.service.CacheCommandService;
import shop.flowchat.chat.common.dto.response.ApiResponse;
import shop.flowchat.chat.common.dto.value.Direction;
import shop.flowchat.chat.command.dto.CacheRequest;
import shop.flowchat.chat.common.dto.response.MessageResponse;
import shop.flowchat.chat.query.MessageQueryService;

import java.util.List;

@RestController
@RequestMapping("/message")
@Tag(name = "메시지 API")
@RequiredArgsConstructor
public class MessageController {
    private final MessageQueryService queryService;
    private final CacheCommandService cacheService;

    @Operation(summary = "방향에 따라 기준 메시지 ID로 메시지 페이지 조회")
    @GetMapping
    public ApiResponse<List<MessageResponse>> getMessages(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestParam UUID chatId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(defaultValue = "up") String direction,
            @RequestParam(defaultValue = "30") int pageSize
    ) {
        Direction dir = Direction.from(direction);
        return ApiResponse.success(queryService.getMessagesByDirection(token, chatId, messageId, dir, pageSize));
    }

    @Operation(summary = "메시지 id(멘션 메시지) 기준 조회")
    @GetMapping("/id")
    public ApiResponse<List<MessageResponse>> getMessagesById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestParam UUID chatId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(defaultValue = "30") int pageSize
    ) {
        return ApiResponse.success(queryService.getMessagesByDirection(token, chatId, messageId, Direction.INCLUSIVE_UP, pageSize));
    }

    @Operation(summary = "최신 메시지 조회(임시)")
    @GetMapping("/latest")
    public ApiResponse<List<MessageResponse>> getLatestMessages(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestParam UUID chatId,
            @RequestParam(defaultValue = "30") int pageSize
    ) {
        return ApiResponse.success(queryService.getLatestMessages(token, chatId, pageSize));
    }

    @Operation(summary = "메시지 캐싱")
    @PostMapping("/cache")
    public ApiResponse<Void> cacheMessagesBatch(@RequestBody CacheRequest request) {
        cacheService.cacheMessagesByUser(request);
        return ApiResponse.success(null);
    }
}