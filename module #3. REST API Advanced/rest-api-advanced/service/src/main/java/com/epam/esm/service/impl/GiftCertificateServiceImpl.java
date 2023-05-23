package com.epam.esm.service.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.TagRepository;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.GiftCertificateAlreadyExistsException;
import com.epam.esm.exception.model.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.mapping.MappingService;
import com.epam.esm.utils.Pageable;
import com.epam.esm.utils.QueryParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.service.validator.PageableValidator.checkParams;
import static com.epam.esm.service.validator.PageableValidator.validate;


@Slf4j
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final MappingService<GiftCertificate, GiftCertificateDTO> certificateMappingService;
    private final MappingService<Tag, TagDTO> tagMappingService;

    @Override
    public GiftCertificateDTO save(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate certificate = certificateMappingService.mapFromDto(giftCertificateDTO);
        if (giftCertificateRepository.isExists(certificate)) {
            throw new GiftCertificateAlreadyExistsException(String.format(
                    "GiftCertificate with given name:[%s] already exists.", giftCertificateDTO.getName()));
        }
        certificate.setCreateDate(LocalDateTime.now());
        certificate.getTags().forEach(tag -> {
            Optional<Tag> tagOpt = tagRepository.findByName(tag.getName());
            tagOpt.ifPresent(value -> tag.setId(value.getId()));
        });

        log.debug("[GiftCertificateService.save()] GiftCertificate with name:[{}] saved.",
                certificate.getName());
        return certificateMappingService.mapToDto(giftCertificateRepository.save(certificate));
    }

    @Override
    public GiftCertificateDTO findById(Long id) {
        if (id == null || id < 1) {
            log.error("[GiftCertificateService.findById()] An exception occurs: id:[{}]" +
                    " can't be less than zero or null", id);
            throw new IllegalArgumentException("GiftCertificate.id can't be less than zero or null");
        }

        GiftCertificateDTO giftCertificateDTO = giftCertificateRepository.findById(id)
                .map(certificateMappingService::mapToDto)
                .orElseThrow(() -> {
                    log.error("[GiftCertificateService.findById()] GiftCertificate for given ID:[{}] not found", id);
                    throw new GiftCertificateNotFoundException(String.format("GiftCertificate not found (id:[%d])", id));
                });

        log.debug("[GiftCertificateService.findById()] GiftCertificate received from database: [{}], for ID:[{}]",
                giftCertificateDTO, id);

        return giftCertificateDTO;
    }

    @Override
    public List<GiftCertificateDTO> findAllByTags(Set<String> tags, Pageable pageable) {
        validate(pageable);
        if (tags == null || tags.isEmpty()) {
            log.error("[GiftCertificateService.findByTags()] An exception occurs: tags names" +
                    " can't be null or empty");
            throw new IllegalArgumentException("Tag names can't be null or empty");
        }
        List<GiftCertificateDTO> certificates = giftCertificateRepository
                .findAllByTags(tags, checkParams(pageable, giftCertificateRepository))
                .stream()
                .map(certificateMappingService::mapToDto)
                .toList();

        if (certificates.isEmpty()) {
            log.error("[GiftCertificateService.findByTags()] GiftCertificate for given tags:[{}] not found",
                    tags);
            throw new GiftCertificateNotFoundException(String.format("GiftCertificate not found (tags:[%s])", tags));
        }

        log.debug("[GiftCertificateService.findByTags()] GiftCertificate received from database: [{}], for tags:[{}]"
                , certificates, tags);
        return certificates;
    }

    @Override
    public List<GiftCertificateDTO> findAllByName(String name, Pageable pageable) {
        validate(pageable);
        Validate.notBlank(name);
        List<GiftCertificateDTO> certificates = giftCertificateRepository
                .findAllByName(name, checkParams(pageable, giftCertificateRepository))
                .stream()
                .map(certificateMappingService::mapToDto)
                .toList();

        if (certificates.isEmpty()) {
            log.error("[GiftCertificateService.findByName()] GiftCertificate for given name:[{}] not found",
                    name);
            throw new GiftCertificateNotFoundException(String.format("GiftCertificate not found (name:[%s])", name));
        }

        log.debug("[GiftCertificateService.findByName()] GiftCertificate received from database: [{}], for name:[{}]"
                , certificates, name);
        return certificates;
    }

    @Override
    public List<GiftCertificateDTO> findAll(Pageable pageable) {
        validate(pageable);
        return giftCertificateRepository.findAll(checkParams(pageable, giftCertificateRepository))
                .stream()
                .map(certificateMappingService::mapToDto)
                .toList();
    }

    @Override
    public List<GiftCertificateDTO> findAllWithParams(QueryParameters queryParams, Pageable pageable) {
        validate(pageable);
        if (queryParams == null) {
            throw new IllegalArgumentException();
        }
        return giftCertificateRepository.findAllWithParams(queryParams, checkParams(pageable, giftCertificateRepository))
                .stream()
                .map(certificateMappingService::mapToDto)
                .toList();
    }

    @Transactional
    @Override
    public GiftCertificateDTO update(GiftCertificateDTO giftCertificateDTO) {
        Validate.notNull(giftCertificateDTO, "GiftCertificateDTO can't be Null");
        if (giftCertificateDTO.getId() < 1 || giftCertificateDTO.getId() == null) {
            log.error("[GiftCertificateService.update()] An exception occurs: given ID:[{}]" +
                    " can't be less than zero or null", giftCertificateDTO.getId());
            throw new IllegalArgumentException("Given ID can't be less than zero or null");
        }

        GiftCertificate giftCertificate = certificateMappingService.mapFromDto(findById(giftCertificateDTO.getId()));
        if (giftCertificateDTO.getName() != null && !giftCertificateDTO.getName().isEmpty()) {
            giftCertificate.setName(giftCertificateDTO.getName());
        }
        if (giftCertificateDTO.getDescription() != null && !giftCertificateDTO.getDescription().isEmpty()) {
            giftCertificate.setDescription(giftCertificateDTO.getDescription());
        }
        if (giftCertificateDTO.getDuration() != null && giftCertificateDTO.getDuration() > 0) {
            giftCertificate.setDuration(giftCertificateDTO.getDuration());
        }
        if (giftCertificateDTO.getPrice() != null && giftCertificateDTO.getPrice() > 0) {
            giftCertificate.setPrice(giftCertificateDTO.getPrice());
        }
        if (giftCertificateDTO.getTags() != null && !giftCertificateDTO.getTags().isEmpty()) {
            Set<Tag> tags = giftCertificateDTO.getTags().stream()
                    .map(tagMappingService::mapFromDto).collect(Collectors.toSet());
            giftCertificate.setTags(tags);
        }
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificateRepository.save(giftCertificate);
        log.debug("[GiftCertificateService.update()] GiftCertificate with ID:[{}] updated.",
                giftCertificateDTO.getId());

        return certificateMappingService.mapToDto(giftCertificate);
    }

    @Override
    public GiftCertificateDTO deleteById(Long id) {
        if (id == null || id < 1) {
            log.error("[GiftCertificateService.deleteById()] " +
                    "An exception occurs: id:[{}] can't be less than zero or null", id);
            throw new IllegalArgumentException("GiftCertificate.id can't be less than zero or null");
        }
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate.isEmpty() || !giftCertificateRepository.isExists(giftCertificate.get())) {
            log.error("[GiftCertificateService.deleteById()] Certificate with given id:[{}] not found.", id);
            throw new GiftCertificateNotFoundException(String
                    .format("Certificate with given id:[%d] not found for delete.", id));
        }

        log.debug("[GiftCertificateService.deleteById()] GiftCertificate for ID:[{}] removed.", id);
        return certificateMappingService.mapToDto(giftCertificateRepository.deleteById(id));
    }
}