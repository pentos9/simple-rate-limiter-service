package com.demo.service.dto.pagination;

import lombok.Data;

@Data
public class SearchReq {
    private String clientIP;

    private Integer pageSize = 10;
    private Integer pageNum = 1;
}
