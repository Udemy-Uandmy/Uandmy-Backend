package com.uandmy.back.common.security;


import com.uandmy.back.user.dto.UserDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 보안 관련 유틸리티
 */
@Slf4j
public class SecurityUtil {

    private SecurityUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 비밀번호 암호화
     * @param value 암호화 대상 문자열
     * @return 암호화 문자열
     */
    public static String encodePassword(String value) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(value);
    }

    /**
     * 비밀번호 확인
     * @param password 암호화 비밀번호
     * @param loginPassword 평문 비밀번호
     * @return 일치 여부
     */
    public static boolean matchPassword(String password, String loginPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(loginPassword, password);
    }


    public static Collection<? extends GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (roles != null) {
            for (String role : roles) {
                // 각 역할을 GrantedAuthority 객체로 변환하여 authorities에 추가
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorities;
    }

    public static void defaultConfig(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);
    }

    public static String getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetailDto user) {
            return user.getUserId();
        }

        return null;
    }
}
