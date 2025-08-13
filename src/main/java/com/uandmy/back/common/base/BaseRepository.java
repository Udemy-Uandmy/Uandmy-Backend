package com.uandmy.back.common.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 기본 Repository 인터페이스
 * @param <T> 엔티티 클래스
 * @param <ID> 아이디 타입
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>{
}

