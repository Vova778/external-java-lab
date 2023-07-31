package com.epam.esm.service;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.TagRepository;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.GiftCertificateNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.mapping.impl.GiftCertificateMappingServiceImpl;
import com.epam.esm.service.mapping.impl.TagMappingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {
    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;
    @Mock
    private TagRepository tagRepository;

    @Mock
    private GiftCertificateMappingServiceImpl certificateMappingService;

    @Mock
    private TagMappingServiceImpl tagMappingService;

    @Test
    void shouldSaveGiftCertificateTest() {

    }

    @Test
    void shouldNotSaveGiftCertificateAlreadyExistsTest() {
  }

    @Test
    void shouldFindGiftCertificateByIdTest() {
  }

    @Test
    void shouldNotFindGiftCertificateByIdNotFoundTest() {
        // Given
        Long giftCertificateId = 1L;
        when(giftCertificateRepository.findById(giftCertificateId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.findById(giftCertificateId));
    }

    @Test
    void shouldUpdateGiftCertificateTest() {
  }

    @Test
    void shouldNotUpdateGiftCertificateNotFoundTest() {
        // Given
        GiftCertificateDTO giftCertificateDTO = createSampleGiftCertificateDTO();
        when(giftCertificateRepository.findById(giftCertificateDTO.getId())).thenReturn(Optional.empty());

        // When, Then
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.update(giftCertificateDTO));
    }

    @Test
    void shouldNotUpdateGiftCertificateInvalidIdTest() {
        // Given
        GiftCertificateDTO giftCertificateDTO = createSampleGiftCertificateDTO();
        giftCertificateDTO.setId(0L);

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.update(giftCertificateDTO));
    }

    @Test
    void shouldUpdatePriceTest() {
    }

    @Test
    void shouldNotUpdatePriceGiftCertificateNotFoundTest() {
        // Given
        Long giftCertificateId = 1L;
        Double newPrice = 99.99;
        when(giftCertificateRepository.findById(giftCertificateId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.updatePrice(giftCertificateId, newPrice));
    }

    @Test
    void shouldNotUpdatePriceInvalidIdTest() {
        // Given
        Long giftCertificateId = 0L;
        Double newPrice = 99.99;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.updatePrice(giftCertificateId, newPrice));
    }

    @Test
    void shouldDeleteGiftCertificateByIdTest() {
        // Given
        Long giftCertificateId = 1L;
        doNothing().when(giftCertificateRepository).deleteById(giftCertificateId);

        // When
        giftCertificateService.deleteById(giftCertificateId);

        // Then
        verify(giftCertificateRepository).deleteById(giftCertificateId);
    }

    @Test
    void shouldNotDeleteGiftCertificateByIdInvalidIdTest() {
        // Given
        Long giftCertificateId = 0L;

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> giftCertificateService.deleteById(giftCertificateId));
    }

    private GiftCertificate createSampleGiftCertificate() {
        return GiftCertificate.builder()
                .id(1L)
                .name("Sample Gift Certificate")
                .description("This is a sample gift certificate.")
                .price(50.0)
                .duration(30)
                .createDate(LocalDateTime.of(2023, Month.JULY, 30, 12, 0))
                .lastUpdateDate(LocalDateTime.of(2023, Month.JULY, 30, 12, 0))
                .tags(createSampleTags()) // Assuming createSampleTags() returns a set of sample tags
                .build();
    }

    private GiftCertificateDTO createSampleGiftCertificateDTO() {
        Set<TagDTO> tagDTOs = createSampleTagDTOs(); // Assuming createSampleTagDTOs() returns a set of sample TagDTOs
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(1L);
        giftCertificateDTO.setName("Sample Gift Certificate");
        giftCertificateDTO.setDescription("This is a sample gift certificate.");
        giftCertificateDTO.setPrice(50.0);
        giftCertificateDTO.setDuration(30);
        giftCertificateDTO.setCreateDate(LocalDateTime.of(2023, Month.JULY, 30, 12, 0));
        giftCertificateDTO.setLastUpdateDate(LocalDateTime.of(2023, Month.JULY, 30, 12, 0));
        giftCertificateDTO.setTags(tagDTOs);
        return giftCertificateDTO;
    }
    private Set<Tag> createSampleTags() {
        Set<Tag> tags = new HashSet<>();

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Tag 1");
        tags.add(tag1);

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Tag 2");
        tags.add(tag2);

        // Add more tags as needed...

        return tags;
    }

    private Set<TagDTO> createSampleTagDTOs() {
        Set<TagDTO> tagDTOs = new HashSet<>();

        TagDTO tagDTO1 = new TagDTO();
        tagDTO1.setId(1L);
        tagDTO1.setName("Tag 1");
        tagDTOs.add(tagDTO1);

        TagDTO tagDTO2 = new TagDTO();
        tagDTO2.setId(2L);
        tagDTO2.setName("Tag 2");
        tagDTOs.add(tagDTO2);

        // Add more TagDTOs as needed...

        return tagDTOs;
    }

}
