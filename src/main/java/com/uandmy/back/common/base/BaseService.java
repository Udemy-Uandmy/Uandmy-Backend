package com.uandmy.back.common.base;


import com.uandmy.back.common.seurity.SecurityUtil;
import com.uandmy.back.common.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class BaseService<D extends BaseDto<T, ID>, T extends BaseEntity, ID> {
    private BaseRepository<T, ID> repository;

    @PostConstruct
    protected abstract void setRepository();

    protected void setRepository(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    protected ApplicationEventPublisher eventPublisher;

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    private UserNameProvider userNameProvider;

//    protected Predicate getPredicate(D searchParam) {
//        return new BooleanBuilder();
//    }

    // getPredicate 대신 getExample 메서드로 변경
    protected Example<T> getExample(D searchParam) {
        T probe = searchParam.toEntity();
        return Example.of(probe);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<D> paging(D searchParam, Pageable pageable) {
        Page<T> entityPage = repository.findAll(getExample(searchParam), pageable);
        return entityPage.map(entity -> {
            D dto = (D) entity.of(searchParam.getClass());
            fillUserNames(dto);
            return dto;
        });
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<D> findAll(D searchParam, Sort sort) {
        if (sort == null) {
            return findAll(searchParam);
        }
        Iterable<T> entityList = repository.findAll(getExample(searchParam), sort);
        return StreamSupport.stream(entityList.spliterator(), false)
                .map(entity -> {
                    D dto = (D) entity.of(searchParam.getClass());
                    fillUserNames(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<D> findAll(D searchParam) {
        Iterable<T> entityList = repository.findAll(getExample(searchParam));
        return StreamSupport.stream(entityList.spliterator(), false)
                .map(entity -> {
                    D dto = (D) entity.of(searchParam.getClass());
                    fillUserNames(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long count(D searchParam) {
        return repository.count(getExample(searchParam));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public ID save(D saveDataDto) {
        String userId = SecurityUtil.getLoginUserId();

        if (saveDataDto.getKey() == null) {
            saveDataDto.setCreatedUserId(userId);
        } else {
            saveDataDto.setUpdatedUserId(userId);
        }

        T save = repository.save(saveDataDto.toEntity());
        return (ID) save.getKey();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public D find(D returnType, ID key) {
        Optional<T> entity = repository.findById(key);
        return entity.map(t -> {
            D dto = (D) t.of(returnType.getClass());
            fillUserNames(dto);
            return dto;
        }).orElse(null);
    }

    @Transactional
    public void delete(D targetDto, ID key) {
        if (targetDto instanceof SoftDeletable) {
            ((SoftDeletable) targetDto).setDeleteAt("Y");
            repository.save(targetDto.toEntity());
        } else {
            repository.deleteById(key);
        }
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public void deleteAll(D targetDto) {
        List<ID> targets = targetDto.getTargets();
        repository.deleteAllById(targets);
    }

    protected void fillUserNames(D dto) {
        if (dto.getCreatedUserId() != null && dto.getCreatedUserName() == null&& !dto.getCreatedUserId().equals("SYSTEM")) {
            dto.setCreatedUserName(userNameProvider.getUsernameByUserId(dto.getCreatedUserId())+"("+dto.getCreatedUserId()+")");
        }
        if (StringUtils.isNotEmpty(dto.getUpdatedUserId()) ) {
            dto.setUpdatedUserName(userNameProvider.getUsernameByUserId(dto.getUpdatedUserId())+"("+dto.getUpdatedUserId()+")");
        }
    }
}
