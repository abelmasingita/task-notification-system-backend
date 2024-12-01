package com.datacentrix.notificationsystem.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private  String userId;
}
