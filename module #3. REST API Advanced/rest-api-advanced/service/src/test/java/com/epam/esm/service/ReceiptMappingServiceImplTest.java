package com.epam.esm.service;

import com.epam.esm.ReceiptRepository;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.ReceiptDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.model.ReceiptNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Receipt;
import com.epam.esm.model.User;
import com.epam.esm.service.impl.ReceiptServiceImpl;
import com.epam.esm.service.mapping.MappingService;
import com.epam.esm.utils.Pageable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReceiptMappingServiceImplTest {
    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private MappingService<Receipt, ReceiptDTO> mappingService;
    @Mock
    private UserService userService;

    @Mock
    private GiftCertificateService giftCertificateService;
    @InjectMocks
    private ReceiptServiceImpl receiptService;
    ReceiptDTO receiptDTO;
    Receipt expectedReceipt;
    long id;
    Double price;
    LocalDateTime createDate;
    String title;
    User user;
    UserDTO userDTO;
    List<ReceiptDTO> dtoList;
    Set<GiftCertificate> giftCertificates;
    Set<GiftCertificateDTO> giftCertificatesDTO;
    @Mock
    private Pageable pageable;
    private GiftCertificateDTO giftCertificateDTO;

    @BeforeEach
    void setUp() {
        pageable = new Pageable(1, 10);
        id = 1L;
        price = 1.5;
        createDate = LocalDateTime.now();
        giftCertificateDTO = new GiftCertificateDTO();
        title = "testTitle";
        giftCertificates = new HashSet<>();
        giftCertificatesDTO = new HashSet<>();
        Long userId = 1L;
        userDTO = new UserDTO(userId, "test@gmail.com", "John", "Doe");
        user = new User(userId, "test@gmail.com", "John", "Doe");
        expectedReceipt = new Receipt(id, title, price, createDate, user, giftCertificates);
        receiptDTO = new ReceiptDTO(id, price, createDate, userDTO, giftCertificatesDTO);
        dtoList = new ArrayList<>(List.of(receiptDTO));
    }

    @AfterEach
    void tearDown() {
        receiptDTO = null;
        giftCertificates.clear();
        giftCertificatesDTO.clear();
        expectedReceipt = null;
    }

    @Test
    public void testSave_Successful() {
        Long userId = 1L;

        when(userService.findById(userId)).thenReturn(userDTO);
        when(mappingService.mapFromDto(receiptDTO)).thenReturn(new Receipt());
        when(receiptRepository.save(any(Receipt.class))).thenReturn(new Receipt());
        when(mappingService.mapToDto(any(Receipt.class))).thenReturn(new ReceiptDTO());

        ReceiptDTO result = receiptService.save(receiptDTO);

        assertNotNull(result);
    }

    @Test
    public void testSave_NullReceiptDTO_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> receiptService.save(null));
    }

    @Test
    public void testFindById_Successful() {
        // Arrange
        Long id = 1L;
        Receipt receipt = new Receipt();
        receipt.setId(id);
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setId(id);

        when(receiptRepository.findById(id)).thenReturn(Optional.of(receipt));
        when(mappingService.mapToDto(receipt)).thenReturn(receiptDTO);

        // Act
        ReceiptDTO result = receiptService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(receiptRepository).findById(id);
        verify(mappingService).mapToDto(receipt);
    }

    @Test
    public void testFindById_InvalidId_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> receiptService.findById(id));
    }

    @Test
    public void testFindById_NotFound_ThrowsReceiptNotFoundException() {
        // Arrange
        Long id = 1L;

        when(receiptRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReceiptNotFoundException.class, () -> receiptService.findById(id));
    }

    @Test
    public void testFindAll_Successful() {

        List<Receipt> receipts = List.of(new Receipt(), new Receipt());
        List<ReceiptDTO> receiptDTOs = List.of(new ReceiptDTO(), new ReceiptDTO());

        when(receiptRepository.findAll(any())).thenReturn(receipts);
        when(mappingService.mapToDto(any(Receipt.class))).thenReturn(new ReceiptDTO());

        List<ReceiptDTO> result = receiptService.findAll(pageable);

        assertNotNull(result);
        assertEquals(receiptDTOs.size(), result.size());
        verify(receiptRepository).findAll(any());
        verify(mappingService, times(receipts.size())).mapToDto(any(Receipt.class));
    }

    @Test
    public void testFindAll_ThrowsReceiptNotFoundException() {
        when(receiptRepository.findAll(any()))
                .thenReturn(Collections.emptyList());

        assertThrows(ReceiptNotFoundException.class,
                () -> receiptService.findAll(pageable));
    }

    @Test
    public void testDeleteById_Successful() {

        when(receiptRepository.findById(id)).thenReturn(Optional.of(expectedReceipt));
        when(receiptRepository.isExists(expectedReceipt)).thenReturn(true);
        when(receiptRepository.deleteById(id)).thenReturn(expectedReceipt);
        when(mappingService.mapToDto(expectedReceipt)).thenReturn(receiptDTO);

        ReceiptDTO result = receiptService.deleteById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(receiptRepository).findById(id);
        verify(receiptRepository).isExists(expectedReceipt);
        verify(receiptRepository).deleteById(id);
        verify(mappingService).mapToDto(expectedReceipt);
    }

    @Test
    public void testDeleteById_InvalidId_ThrowsReceiptNotFoundException() {

        assertThrows(ReceiptNotFoundException.class,
                () -> receiptService.deleteById(id));
    }

    @Test
    public void testDeleteById_NotFound_ThrowsReceiptNotFoundException() {

        when(receiptRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ReceiptNotFoundException.class,
                () -> receiptService.deleteById(id));
    }

    @Test
    public void testDeleteById_NotExists_ThrowsReceiptNotFoundException() {


        when(receiptRepository.findById(id)).thenReturn(Optional.of(expectedReceipt));
        when(receiptRepository.isExists(expectedReceipt)).thenReturn(false);

        assertThrows(ReceiptNotFoundException.class, () -> receiptService.deleteById(id));
    }

    @Test
    public void testSetGiftCertificatesAndPrice_ValidInput_SetsPriceAndGiftCertificates() throws Exception {
        ReceiptDTO receiptDTO = new ReceiptDTO();
        Set<GiftCertificateDTO> giftCertificates = new HashSet<>();
        giftCertificates.add(
                GiftCertificateDTO.builder()
                        .id(1L)
                        .name("Gift 1")
                        .description("Description 1")
                        .price(10.0)
                        .duration(5)
                        .build());
        giftCertificates.add(GiftCertificateDTO.builder()
                .id(2L)
                .name("Gift 2")
                .description("Description 2")
                .price(20.0)
                .duration(10)
                .build());
        receiptDTO.setGiftCertificates(giftCertificates);

        GiftCertificateDTO existingGiftCertificateDTO = GiftCertificateDTO.builder()
                .id(1L)
                .name("Gift 1")
                .description("Description 1")
                .price(10.0)
                .duration(5)
                .build();

        GiftCertificateService giftCertificateService = mock(GiftCertificateService.class);
        when(giftCertificateService.findById(1L)).thenReturn(existingGiftCertificateDTO);

        Method setGiftCertificatesAndPriceMethod = ReceiptServiceImpl.class.getDeclaredMethod("setGiftCertificatesAndPrice", ReceiptDTO.class);
        setGiftCertificatesAndPriceMethod.setAccessible(true);

        setGiftCertificatesAndPriceMethod.invoke(receiptService, receiptDTO);

        double expectedPrice = 30.0;
        assertEquals(expectedPrice, receiptDTO.getPrice(), 0.001);
        assertEquals(giftCertificates, receiptDTO.getGiftCertificates());

        verify(giftCertificateService).findById(1L);
        verifyNoMoreInteractions(giftCertificateService);
    }

}
