package com.epam.esm.service.impl;

import com.epam.esm.UserRepository;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.model.UserNotFoundException;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapping.MappingService;
import com.epam.esm.utils.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.esm.service.validator.PageableValidator.checkParams;
import static com.epam.esm.service.validator.PageableValidator.validate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MappingService<User, UserDTO> mappingService;

    @Override
    public UserDTO save(UserDTO object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserDTO findById(Long id) {
        if (id == null || id < 1) {
            log.error("[UserService.findById()] An exception occurs: id:[{}] can't be less than zero or null", id);
            throw new IllegalArgumentException("An exception occurs: Tag.id can't be less than zero or null");
        }
        UserDTO userDTO = userRepository.findById(id)
                .map(mappingService::mapToDto)
                .orElseThrow(() -> {
                    log.error("[UserService.findById()] User for given ID:[{}] not found", id);
                    throw new UserNotFoundException(String.format("User not found (id:[%d])", id));
                });

        log.debug("[UserService.findById()] User received from database: [{}], for ID:[{}]", userDTO, id);
        return userDTO;
    }


    @Override
    public UserDTO findByName(String name) {
        Validate.notBlank(name);
        UserDTO userDTO = userRepository.findByName(name)
                .map(mappingService::mapToDto)
                .orElseThrow(() -> {
                    log.error("[UserService.findByName()] User for given name:[{}] not found", name);
                    throw new UserNotFoundException(String.format("User not found (name:[%s])", name));
                });

        log.debug("[UserService.findByName()] User received from database: [{}], for name:[{}]", userDTO, name);
        return userDTO;
    }

    @Override
    public List<UserDTO> findAllByName(String name, Pageable pageable) {
        Validate.notBlank(name);
        validate(pageable);
        List<UserDTO> users = userRepository.findAllByName(name, checkParams(pageable, userRepository))
                .stream()
                .map(mappingService::mapToDto)
                .toList();

        if (users.isEmpty()) {
            log.error("[UserService.findByName()] User for given name:[{}] not found",
                    name);
            throw new UserNotFoundException(String.format("User not found (name:[%s])", name));
        }
        return users;
    }

    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        validate(pageable);
        List<UserDTO> userDTOS = userRepository.findAll(checkParams(pageable, userRepository))
                .stream()
                .map(mappingService::mapToDto)
                .toList();
        if (userDTOS.isEmpty()) {
            log.error("[UserService.findAll()] Users not found");
            throw new UserNotFoundException("Users not found");
        }
        log.debug("[UserService.findAll()] Users received from database: [{}]", userDTOS);
        return userDTOS;
    }

    @Override
    public UserDTO deleteById(Long id) {
        throw new UnsupportedOperationException();
    }
}