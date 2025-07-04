package shop.flowchat.chat.client;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import shop.flowchat.chat.dto.common.ApiResponse;
import shop.flowchat.chat.dto.member.request.MemberListRequest;
import shop.flowchat.chat.dto.member.response.MemberResponse;
import shop.flowchat.chat.dto.member.response.MemberSimpleResponse;

@FeignClient(name = "member-service", url = "${chatflow.http-url}")
public interface MemberClient {
    @GetMapping("/members/{memberId}")
    ApiResponse<MemberSimpleResponse> getMemberInfo(@RequestHeader("Authorization") String token, @PathVariable("memberId") UUID memberId);

    @PostMapping("/members/search")
    ApiResponse<MemberResponse> getMemberInfoList(@RequestHeader("Authorization") String token, @Valid @RequestBody MemberListRequest request);
}
