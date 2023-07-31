package com.epam.esm.service;


import com.epam.esm.UserRepository;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.model.UserAlreadyExistsException;
import com.epam.esm.exception.model.UserNotFoundException;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.enums.UserRole;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.service.mapping.impl.UserMappingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Spy
    private UserMappingServiceImpl userMappingService;

    @Test
    void shouldSaveValidUserTest() {
        UserDTO userDTO = createSampleUserDTO();
        User user = createSampleUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userMappingService.mapFromDto(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMappingService.mapToDto(user)).thenReturn(userDTO);

        UserDTO savedUserDTO = userService.save(userDTO);

        assertNotNull(savedUserDTO);
        assertEquals(userDTO.getEmail(), savedUserDTO.getEmail());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void shouldNotSaveUserAlreadyExistsTest() {
        // Given
        UserDTO userDTO = createSampleUserDTO();
        User user = createSampleUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // When, Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(userDTO));
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(user);
    }

    @Test
    void shouldNotSaveInvalidUserTest() {
        // Given
        UserDTO userDTO = new UserDTO(); // Empty userDTO

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> userService.save(userDTO));
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldFindByIdValidIdTest() {
        // Given
        Long userId = 1L;
        User user = createSampleUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMappingService.mapToDto(user)).thenReturn(createSampleUserDTO());

        // When
        UserDTO foundUserDTO = userService.findById(userId);

        // Then
        assertNotNull(foundUserDTO);
        assertEquals("Sample", foundUserDTO.getFirstName());
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldNotFindByIdInvalidIdTest() {
        // Given
        Long userId = -1L;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> userService.findById(userId));
        verify(userRepository, never()).findById(userId);
    }

    @Test
    void shouldNotFindByIdNullIdTest() {
        // Given
        Long userId = null;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> userService.findById(userId));
        verify(userRepository, never()).findById(any());
    }

    @Test
    void shouldFindAllByNameValidNameTest() {
        // Given
        String name = "Sample";
        User user = createSampleUser();
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(userRepository.findAllByFirstNameContainingIgnoreCase(name, pageable)).thenReturn(Collections.singletonList(user));
        when(userMappingService.mapToDto(user)).thenReturn(createSampleUserDTO());
        when(userRepository.countByFirstNameContainingIgnoreCase(name)).thenReturn(1L);

        // When
        Page<UserDTO> resultPage = userService.findAllByName(name, pageable);

        // Then
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        List<UserDTO> resultUsers = resultPage.getContent();
        assertEquals(1, resultUsers.size());
        assertEquals("Sample", resultUsers.get(0).getFirstName());
        verify(userRepository).findAllByFirstNameContainingIgnoreCase(name, pageable);
    }

    @Test
    void shouldNotFindAllByNameNoUsersFoundTest() {
        // Given
        String name = "NonExistent";
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(userRepository.findAllByFirstNameContainingIgnoreCase(name, pageable)).thenReturn(Collections.emptyList());

        // When, Then
        assertThrows(UserNotFoundException.class, () -> userService.findAllByName(name, pageable));
        verify(userRepository).findAllByFirstNameContainingIgnoreCase(name, pageable);
    }

    @Test
    void shouldFindByReceiptValidReceiptIdTest() {
        // Given
        Long receiptId = 1L;
        User user = createSampleUser();
        when(userRepository.findByReceipt(receiptId)).thenReturn(Optional.of(user));
        when(userMappingService.mapToDto(user)).thenReturn(createSampleUserDTO());

        // When
        UserDTO foundUserDTO = userService.findByReceipt(receiptId);

        // Then
        assertNotNull(foundUserDTO);
        assertEquals("Sample", foundUserDTO.getFirstName());
        verify(userRepository).findByReceipt(receiptId);
    }

    @Test
    void shouldNotFindByReceiptInvalidReceiptIdTest() {
        // Given
        Long receiptId = -1L;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> userService.findByReceipt(receiptId));
        verify(userRepository, never()).findByReceipt(receiptId);
    }

    @Test
    void shouldNotFindByReceiptNullReceiptIdTest() {
        // Given
        Long receiptId = null;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> userService.findByReceipt(receiptId));
        verify(userRepository, never()).findByReceipt(any());
    }

    @Test
    void shouldFindAllValidPageableTest() {
        // Given
        User user = createSampleUser();
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(user), pageable, 1L));
        when(userMappingService.mapToDto(user)).thenReturn(createSampleUserDTO());

        // When
        Page<UserDTO> resultPage = userService.findAll(pageable);

        // Then
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        List<UserDTO> resultUsers = resultPage.getContent();
        assertEquals(1, resultUsers.size());
        assertEquals("Sample", resultUsers.get(0).getFirstName());
        verify(userRepository).findAll(pageable);
    }

    @Test
    void shouldNotFindAllNoUsersFoundTest() {
        // Given
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0L));

        // When, Then
        assertThrows(UserNotFoundException.class, () -> userService.findAll(pageable));
        verify(userRepository).findAll(pageable);
    }

    private User createSampleUser() {
        return User.builder()
                .id(1L)
                .email("sample@example.com")
                .firstName("Sample")
                .lastName("User")
                .password("password")
                .userRole(UserRole.CUSTOMER)
                .build();
    }

    private UserDTO createSampleUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .email("sample@example.com")
                .firstName("Sample")
                .lastName("User")
                .password("password")
                .userRole(UserRole.CUSTOMER)
                .build();
    }
}
