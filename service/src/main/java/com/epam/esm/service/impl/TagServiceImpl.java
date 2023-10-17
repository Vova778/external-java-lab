package com.epam.esm.service.impl;

import com.epam.esm.TagRepository;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.TagAlreadyExistsException;
import com.epam.esm.exception.model.TagNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapping.impl.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagDTO save(TagDTO tagDTO) throws TagAlreadyExistsException {
        if (tagDTO == null || tagDTO.getName() == null) {
            log.error("[TagService.save()] An exception occurs: tagDTO can't be  null");
            throw new IllegalArgumentException("An exception occurs: TagDTO can't be null");
        }

        Tag tag = tagMapper.mapFromDto(tagDTO);
        if (tagRepository.existsByName(tag.getName())) {
            log.error("[TagService.save()] Tag with given name:[{}] already exists.", tagDTO.getName());
            throw new TagAlreadyExistsException(String.format("Tag with given name:[%s] already exists.", tagDTO.getName()));
        }
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.mapToDto(savedTag);
    }

    @Override
    public TagDTO findById(Long id) {
        if (id == null || id < 1) {
            log.error("[TagService.findById()] An exception occurs: id:[{}] can't be less than zero or null", id);
            throw new IllegalArgumentException("An exception occurs: Tag.id can't be less than zero or null");
        }

        TagDTO tagDTO = tagRepository.findById(id)
                .map(tagMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error("[TagService.findById()] Tag for given ID:[{}] not found", id);
                    return new TagNotFoundException(String.format("Tag not found (id:[%d])", id));
                });

        log.debug("[TagService.findById()] Tag received from database: [{}], for ID:[{}]", tagDTO, id);
        return tagDTO;
    }

    @Override
    public TagDTO findByName(String name) {
        Validate.notBlank(name);
        TagDTO tagDTO = tagRepository.findByName(name)
                .map(tagMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error("[TagService.findByName()] Tag for given name:[{}] not found", name);
                    return new TagNotFoundException(String.format("Tag not found (name:[%s])", name));
                });

        log.debug("[TagService.findByName()] Tag received from database: [{}], for name:[{}]", tagDTO, name);
        return tagDTO;
    }

    @Override
    public Page<TagDTO> findAllByCertificate(Long certificateID, Pageable pageable) {
        if (certificateID == null || certificateID < 1) {
            log.error("[TagService.findAllByCertificate()] An exception occurs: GiftCertificate.ID:[{}]" +
                    " can't be less than zero or null", certificateID);
            throw new IllegalArgumentException("An exception occurs: Tag.ID can't be less than zero or null");
        }

        List<TagDTO> tags = tagRepository.findAllByCertificate(certificateID, pageable)
                .stream()
                .map(tagMapper::mapToDto)
                .toList();
        if (tags.isEmpty()) {
            log.error("[TagService.findAllByGiftCertificate()] Tags not found");
            throw new TagNotFoundException("Tags not found");
        }
        log.debug("[TagService.findAllByCertificate()] Tags received from database: [{}], for GiftCertificate.ID: [{}]",
                tags, certificateID);
        Long totalRecords = tagRepository.getTotalRecordsForGiftCertificateID(certificateID);
        return new PageImpl<>(tags, pageable, totalRecords);
    }


    @Override
    public TagDTO findMostWidelyUsedTagOfUserWithHighestCostOfAllOrders() {
        return tagRepository.findMostWidelyUsedTagOfUserWithHighestCostOfAllOrders()
                .map(tagMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error("[TagService.findMostWidelyUsedTagOfUserWithHighestCostOfAllOrders()] Tag not found");
                    return new TagNotFoundException("Tag not found");
                });
    }


    @Override
    public Page<TagDTO> findAll(Pageable pageable) {
        List<TagDTO> tags = tagRepository.findAll(pageable)
                .stream()
                .map(tagMapper::mapToDto)
                .toList();
        if (tags.isEmpty()) {
            log.error("[TagService.findAll()] Tags not found");
            throw new TagNotFoundException("Tags not found");
        }
        log.debug("[TagService.findAll()] Tags received from database: [{}]", tags);
        long totalRecords = tagRepository.count();
        return new PageImpl<>(tags, pageable, totalRecords);
    }

    @Override
    public void deleteByID(Long id) {
        if (id == null || id < 1) {
            log.error("[TagService.deleteById()] An exception occurs: id:[{}] can't be less than zero", id);
            throw new IllegalArgumentException("Tag.id can't be less than zero.");
        }

        log.debug("Delete tag given id:[{}] not found.", id);
        if (!tagRepository.existsById(id)) {
            log.error("[TagService.deleteById()] Tag with given id:[{}] not found.", id);
            throw new TagNotFoundException(String.format("Tag with given id:[%d] not found for delete.", id));
        }

        tagRepository.deleteById(id);
        tagRepository.flush();
        log.debug("[TagService.deleteById()] Tag for ID:[{}] removed", id);
    }
}