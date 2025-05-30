package shop.flowchat.chat.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import shop.flowchat.chat.dto.common.ApiResponse;
import shop.flowchat.chat.dto.member.MemberSimpleResponse;

@FeignClient(name = "member-service", url = "${chatflow.http-url}")
public interface MemberClient {
    @GetMapping("/members/{memberId}")
    ApiResponse<MemberSimpleResponse> getMemberInfo(@RequestHeader("Authorization") String token, @PathVariable("memberId") UUID memberId);
}
