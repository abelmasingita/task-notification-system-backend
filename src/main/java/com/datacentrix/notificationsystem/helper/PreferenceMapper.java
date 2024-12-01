package com.datacentrix.notificationsystem.helper;

import com.datacentrix.notificationsystem.dto.NotificationDto;
import com.datacentrix.notificationsystem.dto.PrefernceDTO;
import com.datacentrix.notificationsystem.entity.Preference;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PreferenceMapper {

    @Autowired
    private  ModelMapper modelMapper;

    public PrefernceDTO toDTO(Preference preference) {
        return modelMapper.map(preference, PrefernceDTO.class);
    }
    public List<PrefernceDTO> toDtoList(List<Preference> preferences) {
        return preferences.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    public Preference fromDTO(PrefernceDTO preference) {
        return modelMapper.map(preference, Preference.class);
    }
}
