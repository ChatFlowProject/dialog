package shop.flowchat.chat.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtTokenProvider {

    private final String secretKey = "user_token_for_signature_must_be_at_least_256_bits_in_HMAC_signature_algorithms"; // 실제로는 환경변수로 관리해야 함

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID getMemberIdFromToken(String token) {
        Claims claims = getClaims(token);
        String subject = claims.getSubject();
        return UUID.fromString(subject);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}