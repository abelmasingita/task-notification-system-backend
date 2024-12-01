package com.datacentrix.notificationsystem.helper;

import com.datacentrix.notificationsystem.dto.NotificationDto;
import com.datacentrix.notificationsystem.dto.UserDTO;
import com.datacentrix.notificationsystem.entity.Notification;
import com.datacentrix.notificationsystem.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private  ModelMapper modelMapper;

    public UserDTO toDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO toDtoOptional(Optional<User> user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDTO> toDtoList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public User fromDTO(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
