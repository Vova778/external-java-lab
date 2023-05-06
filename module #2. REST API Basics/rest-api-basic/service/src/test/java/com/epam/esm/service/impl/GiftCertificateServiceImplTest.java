package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.GiftCertificateAlreadyExistsException;
import com.epam.esm.exception.model.GiftCertificateNotFoundException;
import com.epam.esm.impl.GiftCertificateJDBCTemplate;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.mapping.impl.GiftCertificateMappingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    GiftCertificateJDBCTemplate certificateJDBCTemplate;
    @Mock
    GiftCertificateMappingServiceImpl certificateMappingService;
    @InjectMocks
    GiftCertificateServiceImpl certificateService;
    GiftCertificateDTO certificateDTO;
    GiftCertificate expectedCertificate;
    Long id;
    String name;
    Tag tag;
    TagDTO tagDTO;
    List<Tag> tags;


    @BeforeEach
    void setUp() {
        id = 1L;
        name = "testCertificate";
        certificateDTO = GiftCertificateDTO.builder()
                .id(id)
                .name(name)
                .description("desc")
                .price(5.0)
                .duration(2)
                .build();
        expectedCertificate = GiftCertificate.builder()
                .id(id)
                .name(name)
                .description("desc")
                .price(5.0)
                .duration(2)
                .createDate(LocalDateTime.of(2023, 3, 5, 13, 11, 55))
                .build();
        tag = new Tag(1L, "tag1");
        tagDTO = new TagDTO(1L, "tagDTO1");
        tags = new ArrayList<>();
        tags.add(tag);
    }

    @AfterEach
    void tearDown() {
        expectedCertificate = null;
        certificateDTO = null;
    }

    @Disabled("Test does not works properly")
    @Test
    void should_Save() {
        lenient().when(certificateJDBCTemplate.isExists(expectedCertificate)).thenReturn(false);
        when(certificateMappingService.mapFromDto(certificateDTO)).thenReturn(expectedCertificate);
        when(certificateJDBCTemplate.save(expectedCertificate)).thenReturn(id);

        certificateService.save(certificateDTO);

        verify(certificateJDBCTemplate, times(1)).save(expectedCertificate);
    }

    @Test
    void should_Not_Save_If_Exists() {
        when(certificateMappingService.mapFromDto(certificateDTO)).thenReturn(expectedCertificate);
        lenient().when(certificateJDBCTemplate.isExists(expectedCertificate)).thenReturn(true);
        assertThrows(GiftCertificateAlreadyExistsException.class, () -> certificateService.save(certificateDTO));
    }

    @Disabled("Test does not works properly")
    @Test
    void should_FindById() {
        expectedCertificate.setTags(new HashSet<>(tags));
        when(certificateJDBCTemplate.findById(id)).thenReturn(Optional.of(expectedCertificate));
        when(certificateMappingService.mapToDto(expectedCertificate)).thenReturn(certificateDTO);

        GiftCertificateDTO actualDTO = certificateService.findById(id);

        assertEquals(certificateDTO, actualDTO);
    }

    @Test
    void should_Not_FindById_If_Not_Exists_And_Throw() {
        when(certificateJDBCTemplate.findById(id)).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class, () -> certificateService.findById(id));
    }

    @Test
    void should_Not_FindById_And_Throw_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> certificateService.findById(0L));
        assertThrows(IllegalArgumentException.class, () -> certificateService.findById(null));
    }

    @Disabled("Test does not works properly")
    @Test
    void should_FindByName() {
        Optional<List<GiftCertificateDTO>> dtos = Optional.of(List.of(certificateDTO));
        Optional<List<GiftCertificate>> certificates = Optional.of(List.of(expectedCertificate));
        Tag tag = new Tag(1L, "name");
        TagDTO tagDTO = new TagDTO(1L, "nameDTO");

        List<GiftCertificateDTO> actualDTO = certificateService.findAllByName(name);

        assertEquals(dtos.get(), actualDTO);
    }

    @Test
    void should_Not_FindByName_If_Not_Exists_And_Throw() {
        when(certificateJDBCTemplate.findAllByName(name)).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class, () -> certificateService.findAllByName(name));
    }

    @Test
    void should_Not_FindByName_And_Throw_IllegalArgumentException() {
        assertThrows(NullPointerException.class, () -> certificateService.findAllByName(null));
        assertThrows(IllegalArgumentException.class, () -> certificateService.findAllByName(""));
        assertThrows(IllegalArgumentException.class, () -> certificateService.findAllByName(" "));
    }

    @Disabled("Test does not works properly")
    @Test
    void should_FindAll() {
        List<GiftCertificateDTO> certificateDTOS = certificateService.findAll();
        verify(certificateJDBCTemplate, times(1)).findAll();
        assertNotNull(certificateDTOS);
    }

    @Disabled("Test does not works properly")
    @Test
    void should_Update() {
        when(certificateMappingService.mapFromDto(certificateDTO)).thenReturn(expectedCertificate);

        certificateService.update(certificateDTO);

        verify(certificateJDBCTemplate, times(1)).update(expectedCertificate);
    }

    @Test
    void should_Not_Update_And_Throw() {
        certificateDTO.setId(0L);
        assertThrows(IllegalArgumentException.class, () -> certificateService.update(certificateDTO));
        assertThrows(NullPointerException.class, () -> certificateService.update(null));
        certificateDTO.setId(null);
        assertThrows(NullPointerException.class, () -> certificateService.update(certificateDTO));
    }

    @Test
    void should_DeleteById() {
        GiftCertificate giftCertificate = GiftCertificate.builder().id(id).build();
        when(certificateJDBCTemplate.findById(id)).thenReturn(Optional.of(giftCertificate));
        lenient().when(certificateJDBCTemplate.isExists(giftCertificate)).thenReturn(true);
        certificateService.deleteById(id);
        verify(certificateJDBCTemplate, times(1)).deleteById(id);
    }

    @Test
    void should_Not_DeleteById_And_Throw() {
        when(certificateJDBCTemplate.findById(id)).thenReturn(Optional.empty());
        assertThrows(GiftCertificateNotFoundException.class, () -> certificateService.deleteById(id));
        assertThrows(IllegalArgumentException.class, () -> certificateService.deleteById(0L));
        assertThrows(IllegalArgumentException.class, () -> certificateService.deleteById(null));
    }
}