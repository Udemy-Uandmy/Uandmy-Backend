package com.uandmy.back.common.security;


import com.uandmy.back.user.dto.UserDetailDto;
import com.uandmy.back.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long expiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private RedisTemplate<String,String> redisTemplate;


    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SIGNATURE_ALGORITHM, secretKey)
                .compact();
    }

    public String generateToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SIGNATURE_ALGORITHM, secretKey)
                .compact();
    }

    public String generateRefreshToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SIGNATURE_ALGORITHM, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String userId = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        UserDetailDto dto = userRepository.findActiveUserByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 권한은 비워도 되고, 나중에 role 넣고 싶으면 추가 가능
        return new UsernamePasswordAuthenticationToken(
                dto, // principal: userDto 객체 또는 userId
                null, // credentials
                List.of(new SimpleGrantedAuthority("ROLE_" + dto.getRoleId())) // 여기에 권한 추가!
        );
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("토큰 유효성 검사 실패", e);
        }
    }

//    public void storeRefreshToken(String id, String refreshToken) {
//        User user = userRepository.findByUserId(id).orElse(null);
//        if (user != null) {
//            redisTemplate.opsForValue().set(
//                    id,
//                    refreshToken,
//                    refreshExpiration,
//                    TimeUnit.MILLISECONDS
//
//            );
//        }
//    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
