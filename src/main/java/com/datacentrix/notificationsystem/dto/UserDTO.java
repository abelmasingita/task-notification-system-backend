package com.datacentrix.notificationsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserDTO {

    private Long Id;

    private String username;

    private String email;

    private LocalDateTime createdAt ;

    private LocalDateTime updatedAt ;
}
