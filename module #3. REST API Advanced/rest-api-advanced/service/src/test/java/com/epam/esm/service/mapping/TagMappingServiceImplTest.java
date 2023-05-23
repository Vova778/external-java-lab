package com.epam.esm.service.mapping;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.model.Tag;
import com.epam.esm.service.mapping.impl.TagMappingServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagMappingServiceImplTest {

    private final TagMappingServiceImpl tagMappingService = new TagMappingServiceImpl();

    @Test
    public void testMapFromDto() {
        TagDTO dto = new TagDTO(1L, "Tag" );

        Tag model = tagMappingService.mapFromDto(dto);

        assertEquals(dto.getId(), model.getId());
        assertEquals(dto.getName(), model.getName());
    }

    @Test
    public void testMapToDto() {
        Tag model = new Tag(1L, "Tag", new HashSet<>());

        TagDTO dto = tagMappingService.mapToDto(model);

        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getName(), dto.getName());
    }
}
