package com.epam.esm.service;

import com.epam.esm.TagRepository;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.TagAlreadyExistsException;
import com.epam.esm.exception.model.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.mapping.MappingService;
import com.epam.esm.utils.Pageable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagMappingServiceImplTest {
    @Mock
    private TagRepository tagRepository;

    @Mock
    private MappingService<Tag, TagDTO> mappingService;

    @InjectMocks
    private TagServiceImpl tagService;
    TagDTO tagDTO;
    Tag expectedTag;
    long id;
    String name;
    List<TagDTO> dtoList;
    Set<GiftCertificate> giftCertificates;

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "testTag";
        tagDTO = new TagDTO(id, name);
        giftCertificates = new HashSet<>();
        expectedTag = new Tag(id, name, giftCertificates);
        dtoList = new ArrayList<>(List.of(tagDTO));
    }

    @AfterEach
    void tearDown() {
        tagDTO = null;
        giftCertificates.clear();
        expectedTag = null;
    }

    @Test
    public void testSave() throws TagAlreadyExistsException {

        when(mappingService.mapFromDto(tagDTO)).thenReturn(expectedTag);
        when(tagRepository.isExists(expectedTag)).thenReturn(false);
        when(tagRepository.save(expectedTag)).thenReturn(expectedTag);
        when(mappingService.mapToDto(expectedTag)).thenReturn(tagDTO);

        TagDTO result = tagService.save(tagDTO);

        assertEquals(tagDTO, result);
        verify(mappingService).mapFromDto(tagDTO);
        verify(tagRepository).isExists(expectedTag);
        verify(tagRepository).save(expectedTag);
        verify(mappingService).mapToDto(expectedTag);
    }

    @Test
    public void testSave_ThrowsTagAlreadyExistsException() throws TagAlreadyExistsException {

        when(mappingService.mapFromDto(tagDTO)).thenReturn(expectedTag);
        when(tagRepository.isExists(expectedTag)).thenReturn(true);

        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDTO));

    }
    @Test
    void testFindById_ValidId_ReturnsTagDTO() {

        when(tagRepository.findById(id)).thenReturn(Optional.of(expectedTag));
        when(mappingService.mapToDto(expectedTag)).thenReturn(tagDTO);

        // Act
        TagDTO actualTagDTO = tagService.findById(id);

        // Assert
        assertEquals(tagDTO, actualTagDTO);
    }

    @Test
    void testFindById_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.findById(null));
    }

    @Test
    void testFindByName_ValidName_ReturnsTagDTO() {
        when(tagRepository.findByName(name)).thenReturn(Optional.of(expectedTag));
        when(mappingService.mapToDto(expectedTag)).thenReturn(tagDTO);

        TagDTO actualTagDTO = tagService.findByName(name);

        assertEquals(tagDTO, actualTagDTO);
    }

    @Test
    void testFindByName_BlankName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.findByName(" "));
    }

    @Test
    void testFindAll_TagListExists_ReturnsListOfTagDTOs() {
        // Arrange
        Pageable pageable = new Pageable(1, 10);
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag());
        tagList.add(new Tag());

        when(tagRepository.findAll(any())).thenReturn(tagList);
        when(mappingService.mapToDto(any())).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            return new TagDTO();
        });

        List<TagDTO> tagDTOList = tagService.findAll(pageable);
        assertEquals(tagList.size(), tagDTOList.size());
    }

    @Test
    void testFindAll_TagListEmpty_ThrowsTagNotFoundException() {
        Pageable pageable = new Pageable(1, 10);

        when(tagRepository.findAll(any())).thenReturn(Collections.emptyList());

        assertThrows(TagNotFoundException.class, () -> tagService.findAll(pageable));
    }


    @Test
    void testDeleteById_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.deleteById(null));
    }

    @Test
    void testDeleteById_TagNotFound_ThrowsTagNotFoundException() {
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.deleteById(id));
    }

}
