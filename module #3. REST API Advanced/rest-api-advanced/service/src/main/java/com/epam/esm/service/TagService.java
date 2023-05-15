package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

public interface TagService extends GenericService<TagDTO, Long> {
    TagDTO findByName(String name);
}