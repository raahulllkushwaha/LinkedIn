package com.rahul.userservice.dto;

public record LoginResponseDto(String accessToken,
                               Long userId,
                               String email,
                               String name) {
}
