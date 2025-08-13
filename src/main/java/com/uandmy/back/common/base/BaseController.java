package com.uandmy.back.common.base;

import com.uandmy.back.common.utils.CmmnUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Rest BaseController
 *
 * @param <D>  DTO 클래스
 * @param <T>  엔티티 클래스
 * @param <ID> 아이디 타입
 */
public abstract class BaseController<D extends BaseDto<T, ID>, T extends BaseEntity, ID> {

    private BaseService<D, T, ID> service;

    protected BaseController(BaseService<D, T, ID> service) {
        this.service = service;
    }

    /**
     * @param service 서비스 객체
     */
    protected void setService(BaseService<D, T, ID> service) {
        this.service = service;
    }

    /**
     * 데이터 목록을 조회한다.
     *
     * @param searchParam 검색 조건을 가진 dto
     * @return 데이터 목록을 담은 Response 객체
     */
    @GetMapping
    public ResponseEntity<List<D>> findAll(D searchParam, PageParam sortParam) {
        return ResponseEntity.ok(service.findAll(searchParam, sortParam.by()));
    }

    /**
     * 데이터 목록을 조회한다.
     *
     * @param searchParam 검색 조건을 가진 dto
     * @param sortParam   정렬 정보를 가진 클래스
     * @return 데이터 목록을 담은 Response 객체
     */
    @GetMapping(value = "/paging" )
    public  ResponseEntity<Page<D>> paging(D searchParam, PageParam pageParam) {
        return ResponseEntity.ok(service.paging(searchParam, pageParam.of()));
    }

    /**
     * 엔티티 검증
     *
     * @param target 검증 대상 dto
     * @param groups 검증 그룹
     * @return 검증 결과를 담은 Response 객체
     */
    protected List<Map<String, String>> validate(D target, Class<?>... groups) {
        return CmmnUtil.validate(target, groups);
    }

    /**
     * 데이터를 저장한다.
     *
     * @param saveDataDto 저장 데이터를 가진 dto
     * @return 저장된 데이터(or Validation Errors)를 담은 Response 객체
     */
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody D saveDataDto) {
        List<Map<String, String>> errors = validate(saveDataDto);
        if (errors.isEmpty()) {
            return ResponseEntity.ok(service.save(saveDataDto));
        } else {
            return ResponseEntity.badRequest().body(errors);
        }
    }

    /**
     * 데이터를 삭제한다.
     *
     * @param key        삭제 대상 데이터 Primary Key
     * @param returnType return type 설정을 위한 dto
     * @return key(or NotFound)를 담은 Response 객체
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<Object> delete(@PathVariable(value = "key") ID key, D returnType) {
        D data = service.find(returnType, key);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(data, key);
        return ResponseEntity.ok(key);
    }

    /**
     * 여러건의 데이터를 삭제한다.
     *
     * @param target targets(List<ID>)와 prefix를 담은 dto
     * @return 삭제 결과(응답 코드)를 담은 Response 객체
     */
    @PostMapping("/delete")
    public ResponseEntity<Object> deleteAll(@RequestBody D target) {
        if (target.getTargets().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        service.deleteAll(target);
        return ResponseEntity.ok().build();
    }

    /**
     * 데이터를 조회한다.
     *
     * @param key 데이터 일련번호
     * @return 데이터를 담은 Response 객체
     */
    @GetMapping("/{key}")
    public ResponseEntity<D> find(@PathVariable(value = "key") ID key, D returnType) {
        D data = service.find(returnType, key);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }

}
