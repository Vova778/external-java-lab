package com.epam.esm.service;

import com.epam.esm.ReceiptRepository;
import com.epam.esm.dto.ReceiptDTO;
import com.epam.esm.exception.model.ReceiptNotFoundException;
import com.epam.esm.model.entity.Receipt;
import com.epam.esm.service.impl.ReceiptServiceImpl;
import com.epam.esm.service.mapping.impl.ReceiptMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceImplTest {
    @Mock
    private ReceiptRepository receiptRepository;
    @InjectMocks
    private ReceiptServiceImpl receiptService;
    @Mock
    private ReceiptMapper receiptMappingService;

    @Mock
    private UserService userService;

    @Mock
    private GiftCertificateService giftCertificateService;


    @Test
    void shouldSaveReceiptTest() {

    }
    @Test
    void shouldFindReceiptByIdTest() {
        // Given
        Long receiptId = 1L;
        Receipt receipt = new Receipt();
        receipt.setId(receiptId);
        when(receiptRepository.findById(receiptId)).thenReturn(Optional.of(receipt));

        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setId(receiptId);
        when(receiptMappingService.mapToDto(receipt)).thenReturn(receiptDTO);

        // When
        ReceiptDTO foundReceiptDTO = receiptService.findById(receiptId);

        // Then
        assertNotNull(foundReceiptDTO);
        assertEquals(receiptId, foundReceiptDTO.getId());
    }

    @Test
    void shouldThrowExceptionWhenFindReceiptByIdNotFoundTest() {
        // Given
        Long receiptId = 1L;
        when(receiptRepository.findById(receiptId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(ReceiptNotFoundException.class, () -> receiptService.findById(receiptId));
    }

    @Test
    void shouldFindAllReceiptsTest() {

    }

    @Test
    void shouldThrowExceptionWhenFindAllReceiptsNotFoundTest() {
        // Given
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(receiptRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // When, Then
        assertThrows(ReceiptNotFoundException.class, () -> receiptService.findAll(pageable));
    }

    @Test
    void shouldFindAllReceiptsByUserTest() {

    }

    @Test
    void shouldThrowExceptionWhenFindAllReceiptsByUserNotFoundTest() {
        // Given
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(receiptRepository.findAllByUser(userId, pageable)).thenReturn(Collections.emptyList());

        // When, Then
        assertThrows(ReceiptNotFoundException.class, () -> receiptService.findAllByUser(userId, pageable));
    }

    @Test
    void shouldDeleteReceiptByIdTest() {
        // Given
        Long receiptId = 1L;
        Receipt receipt = new Receipt();
        when(receiptRepository.findById(receiptId)).thenReturn(Optional.of(receipt));
        when(receiptRepository.existsById(receiptId)).thenReturn(true);

        // When
        receiptService.deleteById(receiptId);

        // Then
        verify(receiptRepository).deleteById(receiptId);
    }

    @Test
    void shouldThrowExceptionWhenDeleteReceiptByIdNotFoundTest() {
        // Given
        Long receiptId = 1L;
        when(receiptRepository.findById(receiptId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(ReceiptNotFoundException.class, () -> receiptService.deleteById(receiptId));
    }

    @Test
    void shouldThrowExceptionWhenDeleteReceiptByIdExistsFalseTest() {
        // Given
        Long receiptId = 1L;
        Receipt receipt = new Receipt();
        when(receiptRepository.findById(receiptId)).thenReturn(Optional.of(receipt));
        when(receiptRepository.existsById(receiptId)).thenReturn(false);

        // When, Then
        assertThrows(ReceiptNotFoundException.class, () -> receiptService.deleteById(receiptId));
    }


}
