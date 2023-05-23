package com.epam.esm.service.mapping;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.ReceiptDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Receipt;
import com.epam.esm.model.User;
import com.epam.esm.service.mapping.impl.ReceiptMappingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptMappingServiceImplTest {
    @Mock
    private MappingService<GiftCertificate, GiftCertificateDTO> certificateMappingService;

    @Mock
    private MappingService<User, UserDTO> userMappingService;

    private ReceiptMappingServiceImpl receiptMappingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        receiptMappingService = new ReceiptMappingServiceImpl(certificateMappingService, userMappingService);
    }

    @Test
    void testMapFromDto() {
        // Arrange
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setId(1L);
        receiptDTO.setPrice(10.0);
        receiptDTO.setCreateDate(LocalDateTime.now());
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(1L);
        giftCertificateDTO.setName("Gift Certificate 1");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("user@example.com");
        receiptDTO.getGiftCertificates().add(giftCertificateDTO);
        receiptDTO.setUserDTO(userDTO);

        ReceiptMappingServiceImpl mappingService = new ReceiptMappingServiceImpl(certificateMappingService, userMappingService);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Gift Certificate 1");
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(certificateMappingService.mapFromDto(giftCertificateDTO)).thenReturn(giftCertificate);
        when(userMappingService.mapFromDto(userDTO)).thenReturn(user);

        // Act
        Receipt result = mappingService.mapFromDto(receiptDTO);

        // Assert
        assertEquals(receiptDTO.getId(), result.getId());
        assertEquals(receiptDTO.getPrice(), result.getPrice());
        assertEquals(receiptDTO.getCreateDate(), result.getCreateDate());
        assertEquals(user, result.getUser());
        assertEquals(1, result.getGiftCertificates().size());
        assertEquals(giftCertificate, result.getGiftCertificates().iterator().next());

        verify(certificateMappingService).mapFromDto(giftCertificateDTO);
        verify(userMappingService).mapFromDto(userDTO);
        verifyNoMoreInteractions(certificateMappingService, userMappingService);
    }

    @Test
    void testMapToDto() {
        // Arrange
        Receipt receipt = new Receipt();
        receipt.setId(1L);
        receipt.setTitle("Receipt 1");
        receipt.setPrice(10.0);
        receipt.setCreateDate(LocalDateTime.now());
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("Gift Certificate 1");
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        receipt.getGiftCertificates().add(giftCertificate);
        receipt.setUser(user);

        ReceiptMappingServiceImpl mappingService = new ReceiptMappingServiceImpl(certificateMappingService, userMappingService);

        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(1L);
        giftCertificateDTO.setName("Gift Certificate 1");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("user@example.com");

        when(certificateMappingService.mapToDto(giftCertificate)).thenReturn(giftCertificateDTO);
        when(userMappingService.mapToDto(user)).thenReturn(userDTO);

        // Act
        ReceiptDTO result = mappingService.mapToDto(receipt);

        // Assert
        assertEquals(receipt.getId(), result.getId());
        assertEquals(receipt.getPrice(), result.getPrice());
        assertEquals(receipt.getCreateDate(), result.getCreateDate());
        assertEquals(userDTO, result.getUserDTO());
        assertEquals(1, result.getGiftCertificates().size());
        assertEquals(giftCertificateDTO, result.getGiftCertificates().iterator().next());

        verify(certificateMappingService).mapToDto(giftCertificate);
        verify(userMappingService).mapToDto(user);
        verifyNoMoreInteractions(certificateMappingService, userMappingService);
    }
}
