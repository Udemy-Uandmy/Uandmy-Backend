package com.uandmy.back.user.entity;

import com.uandmy.back.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_key")
    private Long userKey;

    private String username; // 사용자 이름
    private String userId; // 로그인 ID (사번)
    private String password; // 암호화된 비밀번호
    private String email;
    private String roleId;
    @Column(name = "use_at", nullable = false, length = 1)
    private String useAt;  // 사용 여부 (Y/N)

    @Column(name = "created_user_id")
    private String createdUserId;  // 생성자 ID

    @Column(name = "updated_user_id")
    private String updatedUserId;  // 수정자 ID

    @CreationTimestamp
    @Column(name = "created_dt", updatable = false)
    private LocalDateTime createdDt; //생성일시

    @Column(name = "updated_dt")
    @UpdateTimestamp
    private LocalDateTime updatedDt; //수정일시

    @Override
    public Object getKey() {
        return userKey;
    }
}
