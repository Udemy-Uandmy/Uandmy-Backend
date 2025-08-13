package com.uandmy.back.user.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.uandmy.back.common.base.BaseDto;
import com.uandmy.back.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class UserDto extends BaseDto<User, Long> {
    public UserDto(Class<User> entityClass) {
        super(User.class);
    }

    public UserDto() {
        super(User.class);
    }

    /**
     * 사용자 Key
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userKey;

    /**
     * 사용자 이름
     */
    private String username;

    /**
     * 사용자 ID
     */
    private String userId;

    /**
     * 패스워드
     */
    private String password;

    /**
     * 역할 ID
     */
    private String roleId;

    /**
     * 부서 ID
     */
    private String deptId;

    /**
     * 이메일
     */
    private String email;

    /**
     * 성별
     */
    private String gender;


    /**
     * 사용 여부
     */
    private String useAt;

    /**
     * 생성자 ID
     */
    private String createdUserId;

    /**
     * 수정자 ID
     */
    private String updatedUserId;

    /**
     * 생성일시
     */
    private LocalDateTime createdDt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedDt;
}

