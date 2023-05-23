package com.epam.esm.service.mapping;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.mapping.impl.GiftCertificateMappingServiceImpl;
import com.epam.esm.service.mapping.impl.TagMappingServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GiftCertificateMappingServiceImplTest {
    private final MappingService<Tag, TagDTO> tagMappingService
            = new TagMappingServiceImpl();
    private final MappingService<GiftCertificate, GiftCertificateDTO> mappingService
            = new GiftCertificateMappingServiceImpl(tagMappingService);

    @Test
    void mapFromDto_ValidGiftCertificateDTO_ReturnsGiftCertificate() {
        // Arrange
        GiftCertificateDTO certificateDTO = GiftCertificateDTO.builder()
                .id(1L)
                .name("Gift 1")
                .description("Description 1")
                .price(10.0)
                .duration(5)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .tags(Set.of(new TagDTO(1L, "Tag 1")))
                .build();

        // Act
        GiftCertificate giftCertificate = mappingService.mapFromDto(certificateDTO);

        // Assert
        assertEquals(certificateDTO.getId(), giftCertificate.getId());
        assertEquals(certificateDTO.getName(), giftCertificate.getName());
        assertEquals(certificateDTO.getDescription(), giftCertificate.getDescription());
        assertEquals(certificateDTO.getPrice(), giftCertificate.getPrice());
        assertEquals(certificateDTO.getDuration(), giftCertificate.getDuration());
        assertEquals(certificateDTO.getTags().size(), giftCertificate.getTags().size());
    }

    @Test
    void mapToDto_ValidGiftCertificate_ReturnsGiftCertificateDTO() {
        // Arrange
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(1L)
                .name("Gift 1")
                .description("Description 1")
                .price(10.0)
                .duration(5)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .tags(Set.of(new Tag(1L, "Tag 1", new HashSet<>())))
                .build();

        GiftCertificateDTO certificateDTO = mappingService.mapToDto(giftCertificate);

        assertEquals(giftCertificate.getId(), certificateDTO.getId());
        assertEquals(giftCertificate.getName(), certificateDTO.getName());
        assertEquals(giftCertificate.getDescription(), certificateDTO.getDescription());
        assertEquals(giftCertificate.getPrice(), certificateDTO.getPrice());
        assertEquals(giftCertificate.getDuration(), certificateDTO.getDuration());
        assertEquals(giftCertificate.getTags().size(), certificateDTO.getTags().size());
    }
}
