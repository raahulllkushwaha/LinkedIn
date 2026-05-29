package com.rahul.userservice.dto;

import lombok.Data;

@Data
public class UserDto {
    Long id;
    private String name;
    private String email;
}
