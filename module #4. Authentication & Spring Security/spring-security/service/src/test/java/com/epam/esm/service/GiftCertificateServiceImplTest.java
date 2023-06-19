package com.epam.esm.service;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.TagRepository;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.GiftCertificateNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.mapping.MappingService;
import com.epam.esm.utils.Pageable;
import com.epam.esm.utils.QueryParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {
    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private MappingService<GiftCertificate, GiftCertificateDTO> certificateMappingService;

    @Mock
    private MappingService<Tag, TagDTO> tagMappingService;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private Long id;
    private GiftCertificateDTO giftCertificateDTO;
    private GiftCertificate giftCertificate;
    private Pageable pageable;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @BeforeEach
    void setUp() {
        name = "Test Certificate";
        description = "Test description";
        price = 10.5;
        duration = 1;
        createDate = LocalDateTime.now();
        lastUpdateDate = LocalDateTime.now();
        pageable = new Pageable(1, 10);
        id = 1L;
        giftCertificateDTO = GiftCertificateDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
        giftCertificate = GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
    }
    @AfterEach
    void tearDown() {
        giftCertificateDTO = null;
        giftCertificate = null;
    }
    @Test
    public void testSave_Successful() {
        when(certificateMappingService.mapFromDto(giftCertificateDTO)).thenReturn(giftCertificate);
        when(giftCertificateRepository.isExists(giftCertificate)).thenReturn(false);
        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
        when(certificateMappingService.mapToDto(giftCertificate)).thenReturn(giftCertificateDTO);

        GiftCertificateDTO result = giftCertificateService.save(giftCertificateDTO);

        assertNotNull(result);
        assertEquals(giftCertificateDTO.getName(), result.getName());
        verify(certificateMappingService).mapFromDto(giftCertificateDTO);
        verify(giftCertificateRepository).isExists(giftCertificate);
        verify(giftCertificateRepository).save(giftCertificate);
        verify(certificateMappingService).mapToDto(giftCertificate);
    }



    @Test
    public void testFindById_Successful() {
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(certificateMappingService.mapToDto(giftCertificate)).thenReturn(giftCertificateDTO);

        GiftCertificateDTO result = giftCertificateService.findById(id);

        assertNotNull(result);
        assertEquals(giftCertificateDTO.getId(), result.getId());
        verify(giftCertificateRepository).findById(id);
        verify(certificateMappingService).mapToDto(giftCertificate);
    }

    @Test
    public void testFindById_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.findById(null));
    }

    @Test
    public void testFindById_GiftCertificateNotFound_ThrowsGiftCertificateNotFoundException() {

        when(giftCertificateRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(GiftCertificateNotFoundException.class,
                () -> giftCertificateService.findById(id));
    }

    @Test
    public void testFindAllByTags_Successful() {
        Set<String> tags = new HashSet<>(Arrays.asList("tag1", "tag2"));
        Pageable pageable = new Pageable(1, 10);
        List<GiftCertificate> giftCertificates = Arrays.asList(
                new GiftCertificate(),
                new GiftCertificate()
        );
        List<GiftCertificateDTO> expectedDtos = Arrays.asList(
                new GiftCertificateDTO(),
                new GiftCertificateDTO()
        );

        when(giftCertificateRepository.findAllByTags(tags, pageable)).thenReturn(giftCertificates);
        when(certificateMappingService.mapToDto(any(GiftCertificate.class))).thenAnswer((Answer<GiftCertificateDTO>) invocation -> {
            GiftCertificate certificate = invocation.getArgument(0);
            int index = giftCertificates.indexOf(certificate);
            return expectedDtos.get(index);
        });

        List<GiftCertificateDTO> result = giftCertificateService.findAllByTags(tags, pageable);

        assertNotNull(result);
        assertEquals(expectedDtos.size(), result.size());
        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedDtos.get(i), result.get(i));
        }
        verify(giftCertificateRepository).findAllByTags(tags, pageable);
        verify(certificateMappingService, times(expectedDtos.size())).mapToDto(any(GiftCertificate.class));
    }

    @Test
    public void testFindAllByTags_NullOrEmptyTags_ThrowsIllegalArgumentException() {
        Set<String> tags1 = null;
        Set<String> tags2 = Collections.emptySet();

        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findAllByTags(tags1, pageable));
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.findAllByTags(tags2, pageable));
    }

    @Test
    public void testFindAllByTags_GiftCertificatesNotFound_ThrowsGiftCertificateNotFoundException() {
        Set<String> tags = new HashSet<>(Arrays.asList("tag1", "tag2"));

        when(giftCertificateRepository.findAllByTags(tags, pageable)).thenReturn(Collections.emptyList());

        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.findAllByTags(tags, pageable));
    }



    @Test
    public void testFindAllByName_NullOrEmptyName_ThrowsIllegalArgumentException() {
        String name2 = "";

        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.findAllByName(name2, pageable));
    }

    @Test
    public void testFindAllByName_GiftCertificatesNotFound_ThrowsGiftCertificateNotFoundException() {

        when(giftCertificateRepository
                .findAllByName(name, pageable)).thenReturn(Collections.emptyList());

        assertThrows(GiftCertificateNotFoundException.class,
                () -> giftCertificateService.findAllByName(name, pageable));
    }

    @Test
    public void testFindAll_ReturnsListOfGiftCertificateDTOs() {
        List<GiftCertificate> giftCertificates = Arrays.asList(new GiftCertificate(), new GiftCertificate());
        List<GiftCertificateDTO> expectedDTOs = Arrays.asList(new GiftCertificateDTO(), new GiftCertificateDTO());
        when(giftCertificateRepository.findAll(any(Pageable.class))).thenReturn(giftCertificates);
        when(certificateMappingService.mapToDto(any(GiftCertificate.class)))
                .thenReturn(expectedDTOs.get(0), expectedDTOs.get(1));

        List<GiftCertificateDTO> result = giftCertificateService.findAll(pageable);

        assertNotNull(result);
        assertEquals(expectedDTOs, result);
    }

    @Test
    public void testFindAllWithParams_NullQueryParams_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.findAllWithParams(null, pageable));
    }

    @Test
    public void testFindAllWithParams_ReturnsListOfGiftCertificateDTOs() {

        QueryParameters queryParams = QueryParameters.builder().build();
        List<GiftCertificate> giftCertificates = Arrays.asList(new GiftCertificate(), new GiftCertificate());
        List<GiftCertificateDTO> expectedDTOs = Arrays.asList(new GiftCertificateDTO(), new GiftCertificateDTO());
        when(giftCertificateRepository.findAllWithParams(any(QueryParameters.class), any(Pageable.class)))
                .thenReturn(giftCertificates);
        when(certificateMappingService.mapToDto(any(GiftCertificate.class)))
                .thenReturn(expectedDTOs.get(0), expectedDTOs.get(1));

        List<GiftCertificateDTO> result = giftCertificateService.findAllWithParams(queryParams, pageable);

        assertNotNull(result);
        assertEquals(expectedDTOs, result);
    }


    @Test
    public void testUpdate_NullGiftCertificateDTO_ThrowsIllegalArgumentException() {
        assertThrows(NullPointerException.class,
                () -> giftCertificateService.update(null));

        verify(certificateMappingService, never()).mapFromDto(any(GiftCertificateDTO.class));
        verify(giftCertificateRepository, never()).save(any(GiftCertificate.class));
        verify(certificateMappingService, never()).mapToDto(any(GiftCertificate.class));
    }
    @Test
    public void testUpdate_InvalidId_ThrowsIllegalArgumentException() {
        giftCertificateDTO.setId(-1L);

        assertThrows(IllegalArgumentException.class,
                () -> giftCertificateService.update(giftCertificateDTO));

        verify(certificateMappingService, never()).mapFromDto(any(GiftCertificateDTO.class));
        verify(giftCertificateRepository, never()).save(any(GiftCertificate.class));
        verify(certificateMappingService, never()).mapToDto(any(GiftCertificate.class));
    }

    @Test
    void testDeleteById_NullId_ThrowsIllegalArgumentException() {
        Long id = null;

        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.deleteById(id));

        verifyNoInteractions(giftCertificateRepository, certificateMappingService);
    }

    @Test
    void testDeleteById_InvalidId_ThrowsGiftCertificateNotFoundException() {
        Long id = 0L;

        when(giftCertificateRepository.findById(0L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.deleteById(id));

        verify(giftCertificateRepository).findById(id);
        verifyNoMoreInteractions(giftCertificateRepository, certificateMappingService);
    }

    @Test
    void testDeleteById_NonExistingGiftCertificate_ThrowsGiftCertificateNotFoundException() {

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(id);

        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateRepository.isExists(giftCertificate)).thenReturn(false);

        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.deleteById(id));

        verify(giftCertificateRepository).findById(id);
        verify(giftCertificateRepository).isExists(giftCertificate);
        verifyNoMoreInteractions(giftCertificateRepository, certificateMappingService);
    }

}
