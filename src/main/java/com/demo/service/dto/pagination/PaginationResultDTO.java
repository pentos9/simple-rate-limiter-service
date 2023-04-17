package com.demo.service.dto.pagination;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResultDTO<T> {
    private Long total;
    private List<T> resultList;

    public PaginationResultDTO(Long total, List<T> resultList) {
        this.total = total;
        this.resultList = resultList;
    }
}
