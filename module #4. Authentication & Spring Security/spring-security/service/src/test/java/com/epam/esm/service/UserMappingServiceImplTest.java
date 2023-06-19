package com.epam.esm.service;


import com.epam.esm.UserRepository;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.model.UserNotFoundException;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.service.mapping.MappingService;
import com.epam.esm.utils.Pageable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserMappingServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private MappingService<User, UserDTO> mappingService;

    @InjectMocks
    private UserServiceImpl userService;
    UserDTO userDTO;
    User user;
    long id;
    String email;
    String firstName;
    String lastName;
    List<UserDTO> dtoList;
    Pageable pageable;


    @BeforeEach
    void setUp() {
        id = 1L;
        firstName = "firstName";
        lastName = "lastName";
        email = "test@gmail.com";
        pageable = new Pageable(1, 10);
        userDTO = new UserDTO(id, email,firstName,lastName);
        user = new User(id, email,firstName,lastName);

        dtoList = new ArrayList<>(List.of(userDTO));
    }

    @AfterEach
    void tearDown() {
        userDTO = null;
        user = null;
    }

    @Test
    public void testFindById_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.findById(null));

        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    public void testFindById_ThrowsUserNotFoundException() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(id));

        verify(userRepository).findById(id);
    }

    @Test
    public void testFindById_Successful() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(mappingService.mapToDto(user)).thenReturn(new UserDTO());

        UserDTO result = userService.findById(id);

        assertNotNull(result);
        verify(userRepository).findById(id);
        verify(mappingService).mapToDto(user);
    }

    @Test
    public void testFindByName_ThrowsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class,
                () -> userService.findByName(" "));

        verify(userRepository, never()).findByName(anyString());
    }

    @Test
    public void testFindByName_ThrowsUserNotFoundException() {
        when(userRepository.findByName(firstName)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByName(firstName));

        verify(userRepository).findByName(firstName);
    }

    @Test
    public void testFindByName_Successful() {
        when(userRepository.findByName(firstName)).thenReturn(Optional.ofNullable(user));
        when(mappingService.mapToDto(user)).thenReturn(userDTO);

        UserDTO result = userService.findByName(firstName);

        assertNotNull(result);
        verify(userRepository).findByName(firstName);
        verify(mappingService).mapToDto(user);
    }
    @Test
    public void testFindAllByName_ValidName_ReturnsListOfUserDTO() {
        String name = "John Doe";

        List<User> users = Arrays.asList(
                new User(),
                new User()
        );
        List<UserDTO> expectedDtos = Arrays.asList(
                new UserDTO(),
                new UserDTO()
        );

        when(userRepository.findAllByName(name, pageable)).thenReturn(users);
        when(mappingService.mapToDto(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    int index = users.indexOf(user);
                    return expectedDtos.get(index);
                });

        List<UserDTO> result = userService.findAllByName(name, pageable);

        assertNotNull(result);
        assertEquals(expectedDtos.size(), result.size());
        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedDtos.get(i), result.get(i));
        }
        verify(userRepository).findAllByName(name, pageable);
        verify(mappingService, times(expectedDtos.size())).mapToDto(any(User.class));
    }

    @Test
    public void testFindAllByName_BlankName_ThrowsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class,
                () -> userService.findAllByName("", pageable));

        verify(userRepository, never()).findAllByName(anyString(), any(Pageable.class));
        verify(mappingService, never()).mapToDto(any(User.class));
    }

    @Test
    public void testFindAll_ValidPageable_ReturnsListOfUserDTO() {
        List<User> users = Arrays.asList(
                new User(),
                new User()
        );
        List<UserDTO> expectedDtos = Arrays.asList(
                new UserDTO(),
                new UserDTO()
        );

        when(userRepository.findAll(any(Pageable.class))).thenReturn(users);
        when(mappingService.mapToDto(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    int index = users.indexOf(user);
                    return expectedDtos.get(index);
                });

        List<UserDTO> result = userService.findAll(pageable);

        assertNotNull(result);
        assertEquals(expectedDtos.size(), result.size());
        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedDtos.get(i), result.get(i));
        }
        verify(userRepository).findAll(pageable);
        verify(mappingService, times(expectedDtos.size())).mapToDto(any(User.class));
    }

    @Test
    public void testFindAll_NullPageable_ThrowsIllegalArgumentException() {
        assertThrows(NullPointerException.class,
                () -> userService.findAll(null));

        verify(userRepository, never()).findAll(any(Pageable.class));
        verify(mappingService, never()).mapToDto(any(User.class));
    }

    @Test
    public void testDeleteById_UnsupportedOperationExceptionThrown() {
        assertThrows(UnsupportedOperationException.class,
                () -> userService.deleteById(id));

        verify(userRepository, never()).deleteById(any(Long.class));
    }
}
