package com.epam.esm.service;

import com.epam.esm.TagRepository;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.TagAlreadyExistsException;
import com.epam.esm.exception.model.TagNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.mapping.impl.TagMappingServiceImpl;
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
public class TagServiceImplTest {
    @InjectMocks
    private TagServiceImpl tagService;
    @Mock
    private TagRepository tagRepository;
    @Spy
    private TagMappingServiceImpl tagMappingService;

    @Test
    void shouldSaveTest(){
        Tag tag = createSampleTag();
        TagDTO tagDTO = createSampleTagDTO();

        when(tagRepository.existsByName("Sample Tag")).thenReturn(false);
        when(tagRepository.save(tag)).thenReturn(tag);

        TagDTO savedTagDTO = tagService.save(tagDTO);

        assertNotNull(savedTagDTO);
        assertEquals("Sample Tag", savedTagDTO.getName());
        verify(tagRepository).save(tag);
    }
    @Test
    void shouldNotSaveTagIfTagAlreadyExistsTest() {
        TagDTO tagDTO = createSampleTagDTO();
        when(tagRepository.existsByName("Sample Tag")).thenReturn(true);

        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDTO));
    }

    @Test
    void shouldNotSaveTagIfInputIncorrectTest() {
        TagDTO tagDTO = new TagDTO();
        assertThrows(IllegalArgumentException.class, () -> tagService.save(null));
        assertThrows(IllegalArgumentException.class, () -> tagService.save(tagDTO));
    }

    @Test
    void shouldNotSaveTagDTOIfNameNullTest() {
        // Given
        TagDTO tagDTO = new TagDTO();

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> tagService.save(tagDTO));
    }

    @Test
    void shouldFindByIdValidIdTest() {
        // Given
        Long id = 1L;
        Tag tag = createSampleTag();
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
        when(tagMappingService.mapToDto(tag)).thenReturn(createSampleTagDTO());

        // When
        TagDTO tagDTO = tagService.findById(id);

        // Then
        assertNotNull(tagDTO);
        assertEquals(id, tagDTO.getId());
        assertEquals("Sample Tag", tagDTO.getName());
        verify(tagRepository).findById(id);
    }

    @Test
    void shouldNotFindByIdInvalidIdTest() {
        Long id = -1L;

        assertThrows(IllegalArgumentException.class, () -> tagService.findById(id));
    }

    @Test
    void shouldNotFindByIdNullIdTest() {
        assertThrows(IllegalArgumentException.class, () -> tagService.findById(null));
    }

    @Test
    void shouldNotFindByIdTagNotFoundTest() {
        Long id = 1L;
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.findById(id));
    }

    @Test
    void shouldFindByNameValidNameTest() {
        // Given
        String tagName = "Sample Tag";
        Tag tag = createSampleTag();
        when(tagRepository.findByName(tagName)).thenReturn(Optional.of(tag));
        when(tagMappingService.mapToDto(tag)).thenReturn(createSampleTagDTO());

        // When
        TagDTO tagDTO = tagService.findByName(tagName);

        // Then
        assertNotNull(tagDTO);
        assertEquals(tagName, tagDTO.getName());
        verify(tagRepository).findByName(tagName);
    }

    @Test
    void shouldNotFindByNameTagNotFoundTest() {
        // Given
        String tagName = "Non-existent Tag";
        when(tagRepository.findByName(tagName)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(TagNotFoundException.class, () -> tagService.findByName(tagName));
    }

    @Test
    void shouldNotFindByNameEmptyNameTest() {
        // When, Then
        assertThrows(IllegalArgumentException.class, () -> tagService.findByName(""));
    }

    @Test
    void shouldFindAllByCertificateValidCertificateIdTest() {
        // Given
        Long certificateId = 1L;
        Tag tag = createSampleTag();
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(tagRepository.findAllByCertificate(certificateId, pageable)).thenReturn(Collections.singletonList(tag));
        when(tagMappingService.mapToDto(tag)).thenReturn(createSampleTagDTO());
        when(tagRepository.getTotalRecordsForGiftCertificateID(certificateId)).thenReturn(1L);

        // When
        Page<TagDTO> resultPage = tagService.findAllByCertificate(certificateId, pageable);

        // Then
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        List<TagDTO> resultTags = resultPage.getContent();
        assertEquals(1, resultTags.size());
        assertEquals("Sample Tag", resultTags.get(0).getName());
        verify(tagRepository).findAllByCertificate(certificateId, pageable);
    }

    @Test
    void shouldNotFindAllByCertificateInvalidCertificateIdTest() {
        // Given
        Long certificateId = -1L;
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> tagService.findAllByCertificate(certificateId, pageable));
    }

    @Test
    void shouldNotFindAllByCertificateNullCertificateIdTest() {
        // Given
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> tagService.findAllByCertificate(null, pageable));
    }

    @Test
    void shouldNotFindAllByCertificateTagsNotFoundTest() {
        // Given
        Long certificateId = 1L;
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(tagRepository.findAllByCertificate(certificateId, pageable)).thenReturn(Collections.emptyList());

        // When, Then
        assertThrows(TagNotFoundException.class, () -> tagService.findAllByCertificate(certificateId, pageable));
    }

    @Test
    void shouldFindAllValidPageableTest() {
        // Given
        Tag tag = createSampleTag();
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(tagRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(tag), pageable, 0));
        when(tagMappingService.mapToDto(tag)).thenReturn(createSampleTagDTO());
        when(tagRepository.count()).thenReturn(1L);

        // When
        Page<TagDTO> resultPage = tagService.findAll(pageable);

        // Then
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        List<TagDTO> resultTags = resultPage.getContent();
        assertEquals(1, resultTags.size());
        assertEquals("Sample Tag", resultTags.get(0).getName());
        verify(tagRepository).findAll(pageable);
    }

    @Test
    void shouldNotFindAllTagsNotFoundTest() {
        // Given
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        when(tagRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // When, Then
        assertThrows(TagNotFoundException.class, () -> tagService.findAll(pageable));
    }

    @Test
    void shouldDeleteTest(){
        Tag tag = createSampleTag();
        when(tagRepository.existsById(tag.getId())).thenReturn(true);
        doNothing().when(tagRepository).deleteById(tag.getId());
        tagService.deleteByID(tag.getId());
        verify(tagRepository, times(1)).deleteById(tag.getId());
    }



    private Tag createSampleTag() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Sample Tag");
        return tag;
    }

    private TagDTO createSampleTagDTO() {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1L);
        tagDTO.setName("Sample Tag");
        return tagDTO;
    }

}
