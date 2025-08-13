package com.uandmy.back.common.base;

import com.uandmy.back.common.utils.MapperUtil;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * 기본 Entity 클래스
 */
@Getter
@MappedSuperclass
public abstract class BaseEntity {

    protected static final String CODE_COLUMN = "VARCHAR(10)";

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT
//    private Long id;

    public abstract Object getKey();

    public <D> D of(Class<D> dto) {
        D data = MapperUtil.getModelMapper().map(this, dto);
        ((BaseDto<?, ?>) data).ofEntity();
        return data;
    }
}
