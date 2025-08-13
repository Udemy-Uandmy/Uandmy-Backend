package com.uandmy.back.common.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uandmy.back.common.utils.MapperUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 기본 Dto 클래스
 * @param <T> 엔티티 클래스
 * @param <ID> 아이디 타입
 */
@Getter
@Setter
public class BaseDto<T, ID> implements Serializable {

    @JsonIgnore
    private final Class<T> entityClass;

    public BaseDto(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public BaseDto(Class<T> entityClass, boolean useSoftDelete) {
        this.entityClass = entityClass;
    }

    public T toEntity() {
        return MapperUtil.getModelMapper().map(this, this.entityClass);
    }

    public void ofEntity() {
        // 개별 DTO 에서 데이터 추가 구현
    }

    /**
     * 생성자 ID
     */
    private String createdUserId;

    /**
     * 생성자 이름
     */
    private String createdUserName;

    /**
     * 수정자 ID
     */
    private String updatedUserId;

    /**
     * 수정자 이름
     */
    private String updatedUserName;


    private List<ID> targets;

    private Object key;
}
