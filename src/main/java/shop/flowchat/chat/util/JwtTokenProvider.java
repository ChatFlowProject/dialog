package shop.flowchat.chat.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private final String secretKey = "user_token_for_signature_must_be_at_least_256_bits_in_HMAC_signature_algorithms";

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public UUID getMemberIdFromToken(String token) {
        Claims claims = getClaims(token);
        String subject = claims.getSubject(); // "UUID:ROLE"
        return UUID.fromString(subject.split(":")[0]);
    }

    // JWT에서 Claims 파싱
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}