package com.epam.esm.service.mapping;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.mapping.impl.UserMappingServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMappingServiceImplTest {

    private final UserMappingServiceImpl userMappingService = new UserMappingServiceImpl();

    @Test
    void testMapFromDto() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");

        User user = userMappingService.mapFromDto(userDTO);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getLastName(), user.getLastName());
    }

    @Test
    void testMapToDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        UserDTO userDTO = userMappingService.mapToDto(user);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getLastName(), userDTO.getLastName());
    }
}