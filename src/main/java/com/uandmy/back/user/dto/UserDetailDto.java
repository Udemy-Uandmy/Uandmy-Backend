package com.uandmy.back.user.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailDto {
    private Long userKey;
    private String username; // 사용자 이름
    private String userId; // 로그인 ID (사번)
    private String password; // 암호화된 비밀번호
    private String email;
    private String roleId;
    private char useAt;
}

