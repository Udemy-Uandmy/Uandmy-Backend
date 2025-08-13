package com.uandmy.back.common.base;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 페이징 처리를 위한 파라미터 설정 클래스
 */
public final class PageParam implements Serializable {

    private int page = 1;

    private int size = 10;

    private Sort.Direction direction = Sort.Direction.ASC;

    @Setter
    private String sort;

    @Getter
    private String orders;

    private List<Sort.Order> orderList;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int maxSize = 300;
        this.size = Math.min(size, maxSize);
    }

    public void setDirection(String direction) {
        if (StringUtils.equals(direction, "asc")) {
            this.direction = Sort.Direction.ASC;
        } else {
            this.direction = Sort.Direction.DESC;
        }
    }

    public void setOrders(String orders) {
        this.orders = orders;
        if (StringUtils.isNotEmpty(orders)) {
            String[] arrOrder = StringUtils.split(orders, ",");
            orderList = new ArrayList<>();
            for (String order : arrOrder) {
                String[] arrO = StringUtils.split(StringUtils.trim(order), " ");
                if (StringUtils.equals(arrO[1], "desc")) {
                    orderList.add(Sort.Order.desc(arrO[0]));
                } else {
                    orderList.add(Sort.Order.asc(arrO[0]));
                }
            }
        }
    }

    public PageRequest of() {
        if (StringUtils.isNotEmpty(orders)) {
            return PageRequest.of(page -1, size, Sort.by(orderList));
        }
        if (StringUtils.isNotEmpty(sort)) {
            return PageRequest.of(page -1, size, direction, sort);
        }
        return PageRequest.of(page - 1, size);
    }

    public Sort by() {
        if (StringUtils.isNotEmpty(orders)) {
            return Sort.by(orderList);
        }
        if (StringUtils.isNotEmpty(sort)) {
            return Sort.by(direction, sort);
        }
        return null;
    }
}
