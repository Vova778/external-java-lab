package com.epam.esm.service.mapping.impl;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.User;
import com.epam.esm.service.mapping.MappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserMappingServiceImpl implements MappingService<User, UserDTO> {
    @Override
    public User mapFromDto(UserDTO dto) {
        User model = new User();
        BeanUtils.copyProperties(dto, model);
        log.debug("[UserMappingService] UserDTO converted to User model: [{}]", model);
        return model;
    }

    @Override
    public UserDTO mapToDto(User model) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(model, dto);
        log.debug("[UserMappingService] User model converted to UserDTO: [{}]", dto);
        return dto;
    }
}