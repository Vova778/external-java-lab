package com.epam.esm.service.impl;

import com.epam.esm.UserRepository;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.model.UserAlreadyExistsException;
import com.epam.esm.exception.model.UserNotFoundException;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapping.MappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MappingService<User, UserDTO> mappingService;

    @Override
    public UserDTO save(UserDTO userDTO) {
        if (userDTO == null || userDTO.getEmail() == null) {
            log.error("[UserService.save()] An exception occurs: UserDTO can't be  null");
            throw new IllegalArgumentException("An exception occurs: UserDTO can't be null");
        }

        User user = mappingService.mapFromDto(userDTO);

        if (userRepository.isExistsByEmail(user)) {
            log.error("[UserService.save()] User with given email:[{}] already exists.", userDTO.getEmail());
            throw new UserAlreadyExistsException(String
                    .format("User with given email:[%s] already exists.", userDTO.getEmail()));
        }
        User savedUser = userRepository.save(user);
        return mappingService.mapToDto(savedUser);
    }

    @Override
    public UserDTO findById(Long id) {
        if (id == null || id < 1) {
            log.error("[UserService.findById()] An exception occurs: id:[{}] can't be less than zero or null", id);
            throw new IllegalArgumentException("An exception occurs: Tag.id can't be less than zero or null");
        }

        UserDTO userDTO = userRepository.findByID(id)
                .map(mappingService::mapToDto)
                .orElseThrow(() -> {
                    log.error("[UserService.findById()] User for given ID:[{}] not found", id);
                    throw new UserNotFoundException(String.format("User not found (id:[%d])", id));
                });

        log.debug("[UserService.findById()] User received from database: [{}], for ID:[{}]", userDTO, id);
        return userDTO;
    }

    @Override
    public Page<UserDTO> findAllByName(String name, Pageable pageable) {
        Validate.notBlank(name);
        List<UserDTO> users = userRepository.findAllByName(name, pageable)
                .stream()
                .map(mappingService::mapToDto)
                .toList();

        if (users.isEmpty()) {
            log.error("[UserService.findByName()] User for given name:[{}] not found",
                    name);
            throw new UserNotFoundException(String.format("User not found (name:[%s])", name));
        }
        Long totalRecords = userRepository.getTotalRecordsForNameLike(name);
        return new PageImpl<>(users, pageable, totalRecords);
    }

    @Override
    public UserDTO findByReceipt(Long receiptID) {
        if (receiptID == null || receiptID < 1) {
            log.error("[UserService.findByReceipt()] An exception occurs: Receipt.ID:[{}] can't be less than zero or null",
                    receiptID);
            throw new IllegalArgumentException("An exception occurs: Receipt.ID can't be less than zero or null");
        }
        UserDTO userDTO = userRepository.findByReceipt(receiptID)
                .map(mappingService::mapToDto)
                .orElseThrow(() -> {
                    log.error("[UserService.findByReceipt()] User for given Receipt.ID:[{}] not found", receiptID);
                    throw new UserNotFoundException(String.format("User not found (Receipt.ID:[%d])", receiptID));
                });

        log.debug("[UserService.findByReceipt()] User received from database: [{}], for Receipt.ID:[{}]", userDTO,
                receiptID);
        return userDTO;
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        List<UserDTO> users = userRepository.findAll(pageable)
                .stream()
                .map(mappingService::mapToDto)
                .toList();
        if (users.isEmpty()) {
            log.error("[UserService.findAll()] Users not found");
            throw new UserNotFoundException("Users not found");
        }
        log.debug("[UserService.findAll()] Users received from database: [{}]", users);
        Long totalRecords = userRepository.getTotalRecords();
        return new PageImpl<>(users, pageable, totalRecords);
    }
}