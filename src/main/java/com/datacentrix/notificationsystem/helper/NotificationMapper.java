package com.datacentrix.notificationsystem.helper;

import com.datacentrix.notificationsystem.dto.NotificationDto;
import com.datacentrix.notificationsystem.entity.Notification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    @Autowired
    private  ModelMapper modelMapper;

    public NotificationDto toDTO(Notification notification) {
        return modelMapper.map(notification, NotificationDto.class);
    }
    public List<NotificationDto> toDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    public Notification fromDTO(NotificationDto notification) {
        return modelMapper.map(notification, Notification.class);
    }
}
