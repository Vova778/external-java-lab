package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TagService  {
    TagDTO save(TagDTO tagDTO);

    TagDTO findById(Long id);

    Page<TagDTO> findAll(Pageable pageable);

    TagDTO findByName(String name);

    TagDTO findMostWidelyUsedTagOfUserWithHighestCostOfAllOrders();

    Page<TagDTO> findAllByCertificate(Long certificateID, Pageable pageable);

    void deleteByID(Long id);
}