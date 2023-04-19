package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.TagAlreadyExistsException;
import com.epam.esm.exception.model.TagNotFoundException;
import com.epam.esm.impl.TagJDBCTemplate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.mapping.impl.TagMappingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    @Mock
    TagJDBCTemplate tagJDBCTemplate;
    @Mock
    TagMappingServiceImpl mappingService;
    @InjectMocks
    TagServiceImpl tagService;
    TagDTO tagDTO;
    Tag expectedTag;
    long id;
    String name;
    List<TagDTO> dtoList;


    @BeforeEach
    void setUp() {
        id = 1L;
        name = "testTag";
        tagDTO = new TagDTO(id, name);
        expectedTag = new Tag(id, name);
        dtoList = new ArrayList<>(List.of(tagDTO));
    }

    @AfterEach
    void tearDown() {
        tagDTO = null;
        expectedTag = null;
    }

    @Test
    void should_Save() {
        when(mappingService.mapFromDto(tagDTO)).thenReturn(expectedTag);
        when(tagJDBCTemplate.save(expectedTag)).thenReturn(id);

        tagService.save(tagDTO);

        verify(tagJDBCTemplate, times(1)).save(expectedTag);
    }

    @Test
    void should_Not_Save_If_Exists() {
        when(mappingService.mapFromDto(tagDTO)).thenReturn(expectedTag);
        when(tagJDBCTemplate.isExists(expectedTag)).thenReturn(true);
        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDTO));
    }

    @Test
    void should_FindById() {
        when(tagJDBCTemplate.findById(id)).thenReturn(Optional.of(expectedTag));
        when(mappingService.mapToDto(expectedTag)).thenReturn(tagDTO);

        TagDTO tagDTOExpected = tagService.findById(id);

        assertEquals(tagDTO, tagDTOExpected);
    }

    @Test
    void should_Not_FindById_If_Not_Exists_And_Throw() {
        when(tagJDBCTemplate.findById(id)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.findById(id));
    }

    @Test
    void should_Not_FindById_And_Throw_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tagService.findById(0L));
        assertThrows(IllegalArgumentException.class, () -> tagService.findById(null));
    }


    @Test
    void should_FindByName() {
        Optional<List<Tag>> tags = Optional.of(new ArrayList<>());
        tags.get().add(expectedTag);
        when(tagJDBCTemplate.findAllByName(name)).thenReturn(tags);
        when(mappingService.mapToDto(expectedTag)).thenReturn(tagDTO);

        List<TagDTO> tagDTOExpected = tagService.findAllByName(name);

        assertEquals(dtoList, tagDTOExpected);
    }

    @Test
    void should_Not_FindByName_If_Not_Exists_And_Throw() {
        when(tagJDBCTemplate.findAllByName(name)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.findAllByName(name));
    }

    @Test
    void should_Not_FindByName_And_Throw_IllegalArgumentException() {
        assertThrows(NullPointerException.class, () -> tagService.findAllByName(null));
        assertThrows(IllegalArgumentException.class, () -> tagService.findAllByName(""));
        assertThrows(IllegalArgumentException.class, () -> tagService.findAllByName(" "));
    }

    @Test
    void should_FindAll() {
        List<TagDTO> tagDTOS = tagService.findAll();
        verify(tagJDBCTemplate, times(1)).findAll();
        assertNotNull(tagDTOS);
    }

    @Test
    void should_DeleteById() {
        Tag tag = new Tag(id, "test");
        when(tagJDBCTemplate.findById(id)).thenReturn(Optional.of(tag));
        lenient().when(tagJDBCTemplate.isExists(tag)).thenReturn(true);
        tagService.deleteById(id);
        verify(tagJDBCTemplate, times(1)).deleteById(id);
    }

    @Test
    void should_Not_DeleteById_And_Throw() {
        when(tagJDBCTemplate.findById(id)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.deleteById(id));
        assertThrows(IllegalArgumentException.class, () -> tagService.deleteById(0L));
        assertThrows(IllegalArgumentException.class, () -> tagService.deleteById(null));
    }
}
