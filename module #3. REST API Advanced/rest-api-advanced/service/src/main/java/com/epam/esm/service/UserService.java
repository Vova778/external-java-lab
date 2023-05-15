package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.utils.Pageable;

import java.util.List;

public interface UserService extends GenericService<UserDTO, Long> {
    UserDTO findByName(String name);
    List<UserDTO> findAllByName(String name, Pageable pageable);
}