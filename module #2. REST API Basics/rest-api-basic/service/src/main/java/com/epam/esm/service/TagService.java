package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

import java.util.List;

public interface TagService {
    void save(TagDTO tagDTO);

    TagDTO findById(Long id);

    List<TagDTO> findAllByName(String name);

    List<TagDTO> findAll();

    void deleteById(Long id);
}
