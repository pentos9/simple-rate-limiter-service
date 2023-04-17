package com.demo.web.rest.common;

import lombok.Data;

@Data
public class Pagination {
    private Integer pageSize;
    private Integer pageNumber;

    public Pagination(Integer pageSize, Integer pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.calculate();
    }

    private void calculate() {
        if (this.pageSize == null) {
            this.pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        if (this.pageNumber == null) {
            this.pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }
    }
}
