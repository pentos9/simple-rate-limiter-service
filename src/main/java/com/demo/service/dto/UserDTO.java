package com.demo.service.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String address;
    private String phoneNumber;
}
