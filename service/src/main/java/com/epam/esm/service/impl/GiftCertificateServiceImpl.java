package com.epam.esm.service.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.TagRepository;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.model.GiftCertificateAlreadyExistsException;
import com.epam.esm.exception.model.GiftCertificateNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.mapping.MappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


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
        if (giftCertificateDTO == null || giftCertificateDTO.getName() == null) {
            log.error("[GiftCertificateService.save()] An exception occurs: giftCertificateDTO can't be  null");
            throw new IllegalArgumentException("An exception occurs: giftCertificateDTO can't be null");
        }

        GiftCertificate certificate = certificateMappingService.mapFromDto(giftCertificateDTO);
        if (giftCertificateRepository.existsByName(certificate.getName())) {
            log.error("[GiftCertificateService.save()] GiftCertificate with given name:[{}] already exists.",
                    giftCertificateDTO.getName());
            throw new GiftCertificateAlreadyExistsException(String.format(
                    "GiftCertificate with given name:[%s] already exists.", giftCertificateDTO.getName()));
        }
        LocalDateTime creationTime = LocalDateTime.now(ZoneOffset.UTC);
        certificate.setCreateDate(creationTime);
        certificate.setLastUpdateDate(creationTime);

        certificate.getTags().forEach(tag -> {
            Optional<Tag> tagOpt = tagRepository.findByName(tag.getName());
            tagOpt.ifPresent(value -> tag.setId(value.getId()));
        });


        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(certificate);
        log.debug("[GiftCertificateService.save()] GiftCertificate saved :[{}].", savedGiftCertificate.getName());
        return certificateMappingService.mapToDto(savedGiftCertificate);
    }

    @Override
    public GiftCertificateDTO findById(Long id) {
        if (id == null || id < 1) {
            log.error("[GiftCertificateService.findById()] An exception occurs: id:[{}] can't be less than zero or null",
                    id);
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
    public Page<GiftCertificateDTO> findAllByTags(Set<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            log.error("[GiftCertificateService.findByTags()] An exception occurs: tags names" +
                    " can't be null or empty");
            throw new IllegalArgumentException("Tag names can't be null or empty");
        }

        List<GiftCertificateDTO> certificates = giftCertificateRepository
                .findAllByTags(tags, pageable)
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

        Long totalRecords = giftCertificateRepository.getTotalRecordsForTagsParam(tags);
        log.debug("[GiftCertificateService.findByTags()] Total records for tags:[{}]", totalRecords);
        return new PageImpl<>(certificates, pageable, totalRecords);
    }

    @Override
    public Page<GiftCertificateDTO> findAllByName(String name, Pageable pageable) {
        Validate.notBlank(name);
        List<GiftCertificateDTO> certificates = giftCertificateRepository
                .findAllByNameContainingIgnoreCase(name, pageable)
                .stream()
                .map(certificateMappingService::mapToDto)
                .toList();

        if (certificates.isEmpty()) {
            log.error("[GiftCertificateService.findByName()] GiftCertificate for given name:[{}] not found",
                    name);
            throw new GiftCertificateNotFoundException(String.format("GiftCertificate not found (name:[%s])", name));
        }

        log.debug("[GiftCertificateService.findByName()] GiftCertificates received from database: [{}], for name:[{}]"
                , certificates, name);
        Long totalRecords = giftCertificateRepository.countByNameContainingIgnoreCase(name);
        log.debug("[GiftCertificateService.findByName()] Total records for name:[{}]", totalRecords);
        return new PageImpl<>(certificates, pageable, totalRecords);
    }

    @Override
    public Page<GiftCertificateDTO> findAll(Pageable pageable) {
        List<GiftCertificateDTO> certificates = giftCertificateRepository
                .findAll(pageable)
                .stream()
                .map(certificateMappingService::mapToDto)
                .toList();
        if (certificates.isEmpty()) {
            log.error("[GiftCertificateService.findAll()] GiftCertificates not found");
            throw new GiftCertificateNotFoundException("GiftCertificates not found");
        }

        log.debug("[GiftCertificateService.findAll()] GiftCertificates received from database: [{}]", certificates);
        long totalRecords = giftCertificateRepository.count();
        return new PageImpl<>(certificates, pageable, totalRecords);

    }



    @Override
    public Page<GiftCertificateDTO> findAllByReceipt(Long receiptID, Pageable pageable) {
        if (receiptID == null || receiptID < 1) {
            log.error("[GiftCertificateService.findAllByReceipt()] An exception occurs: Receipt.ID:[{}]" +
                    " can't be less than zero or null", receiptID);
            throw new IllegalArgumentException("An exception occurs: Receipt.ID can't be less than zero or null");
        }
        List<GiftCertificateDTO> giftCertificates = giftCertificateRepository
                .findAllByReceipt(receiptID, pageable)
                .stream()
                .map(certificateMappingService::mapToDto)
                .toList();
        if (giftCertificates.isEmpty()) {
            log.error("[GiftCertificateService.findAllByReceipt()] GiftCertificates not found");
            throw new GiftCertificateNotFoundException("GiftCertificates not found");
        }
        log.debug("[GifCertificateService.findAllByReceipt()] GiftCertificates received from database: [{}], for Receipt.ID: [{}]",
                giftCertificates, receiptID);
        Long totalRecords = giftCertificateRepository.getTotalRecordsForReceiptID(receiptID);
        log.debug("[GifCertificateService.findAllByReceipt()] Total records for receipt.ID:[{}]", totalRecords);
        return new PageImpl<>(giftCertificates, pageable, totalRecords);
    }

    @Override
    public GiftCertificateDTO update(GiftCertificateDTO giftCertificateDTO) {
        Validate.notNull(giftCertificateDTO, "GiftCertificateDTO can't be Null");

        if (giftCertificateDTO.getId() < 1) {
            log.error("[GiftCertificateService.update()] An exception occurs: given ID:[{}]" +
                    " can't be less than zero or null", giftCertificateDTO.getId());
            throw new IllegalArgumentException("Given ID can't be less than zero or null");
        }

        log.debug("[GiftCertificateService.update()] GiftCertificateDTO for update: [{}]", giftCertificateDTO);
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

        giftCertificate.getTags().forEach(tag -> {
            Optional<Tag> tagOpt = tagRepository.findByName(tag.getName());
            tagOpt.ifPresent(value -> tag.setId(value.getId()));
        });

        LocalDateTime lastUpdate = LocalDateTime.now(ZoneOffset.UTC);
        giftCertificate.setLastUpdateDate(lastUpdate);
        giftCertificateRepository.save(giftCertificate);
        log.debug("[GiftCertificateService.update()] GiftCertificate with ID:[{}] updated.",
                giftCertificateDTO.getId());

        return certificateMappingService.mapToDto(giftCertificate);
    }

    @Override
    public GiftCertificateDTO updatePrice(Long giftCertificateID, Double price) {
        Validate.notNull(giftCertificateID, "GiftCertificateID can't be Null");
        Validate.notNull(price, "New price can't be Null");

        if (giftCertificateID < 1 || price <= 0) {
            log.error("[GiftCertificateService.updatePrice()] An exception occurs: given ID[{}] or Price:[{}]" +
                    " can't be less than zero", giftCertificateID, price);
            throw new IllegalArgumentException("Given ID or Price can't be less than zero");
        }

        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateID)
                .orElseThrow(() -> {
                    log.error("[GiftCertificateService.updatePrice()] GiftCertificate for given ID:[{}] not found", giftCertificateID);
                    throw new GiftCertificateNotFoundException(String.format("GiftCertificate not found (id:[%d])", giftCertificateID));
                });

        giftCertificate.setPrice(price);
        LocalDateTime lastUpdate = LocalDateTime.now(ZoneOffset.UTC);
        giftCertificate.setLastUpdateDate(lastUpdate);

        GiftCertificate withUpdatedPrice = giftCertificateRepository.save(giftCertificate);
        log.debug("[GiftCertificateService.updatePrice()] Price has been updated.");
        return certificateMappingService.mapToDto(withUpdatedPrice);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null || id < 1) {
            log.error("[GiftCertificateService.deleteById()] " +
                    "An exception occurs: id:[{}] can't be less than zero or null", id);
            throw new IllegalArgumentException("GiftCertificate.id can't be less than zero or null");
        }

        giftCertificateRepository.deleteById(id);

        log.debug("[GiftCertificateService.deleteById()] GiftCertificate for ID:[{}] removed.", id);
    }

}