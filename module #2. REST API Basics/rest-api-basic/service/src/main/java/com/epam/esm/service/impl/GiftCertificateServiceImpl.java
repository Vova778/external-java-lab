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
import com.epam.esm.utils.QueryParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class implements functionality of operating {@link GiftCertificateRepository}
 * and {@link TagRepository} methods in according to received
 * parameters from GiftCertificate controller
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final MappingService<GiftCertificate, GiftCertificateDTO> certificateMappingService;
    private final MappingService<Tag, TagDTO> tagMappingService;

    private final TransactionTemplate transactionTemplate;

    @Transactional
    @Override
    public void save(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate certificate = certificateMappingService.mapFromDto(giftCertificateDTO);
        if (giftCertificateRepository.isExists(certificate)) {
            log.error("[GiftCertificateService.save()] GiftCertificate with given name:[{}] already exists.",
                    giftCertificateDTO.getName());
            throw new GiftCertificateAlreadyExistsException(String.format(
                    "GiftCertificate with given name:[%s] already exists.", giftCertificateDTO.getName()));
        }
        certificate.setCreateDate(LocalDateTime.now());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    certificate.setId(giftCertificateRepository.save(certificate));
                    attachAndSaveTags(certificate);
                    log.debug("[GiftCertificateService.save()] GiftCertificate with name:[{}] saved.",
                            certificate.getName());
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    log.error("[GiftCertificateService.save()] rollback !!!");
                }
            }
        });
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

        List<TagDTO> tags = getMappedAndCollected(giftCertificateDTO);
        giftCertificateDTO.setTags(new HashSet<>(tags));

        log.debug("[GiftCertificateService.findById()] GiftCertificate received from database: [{}], for ID:[{}]",
                giftCertificateDTO, id);

        return giftCertificateDTO;
    }

    @Override
    public List<GiftCertificateDTO> findAllByName(String name) {
        Validate.notBlank(name);
        List<GiftCertificateDTO> certificates = giftCertificateRepository.findAllByName(name)
                .stream()
                .flatMap(Collection::stream)
                .map(certificateMappingService::mapToDto)
                .collect(Collectors.toList());

        if (certificates.isEmpty()) {
            log.error("[GiftCertificateService.findByName()] GiftCertificate for given name:[{}] not found",
                    name);
            throw new GiftCertificateNotFoundException(String.format("GiftCertificate not found (name:[%s])", name));
        } else {
            for (GiftCertificateDTO certificateDTO : certificates) {
                List<TagDTO> tags = getMappedAndCollected(certificateDTO);
                certificateDTO.setTags(new HashSet<>(tags));
            }
            log.debug("[GiftCertificateService.findByName()] GiftCertificate received from database: [{}], for name:[{}]"
                    , certificates, name);
            return certificates;
        }
    }

    @Override
    public List<GiftCertificateDTO> findAll() {
        List<GiftCertificateDTO> certificates = giftCertificateRepository.findAll()
                .stream()
                .flatMap(Collection::stream)
                .map(certificateMappingService::mapToDto)
                .collect(Collectors.toList());
        return getCertificateDTOSWithTags(certificates);
    }

    @Override
    public List<GiftCertificateDTO> findAllWithParams(QueryParameters queryParameters) {
        if (queryParameters == null) {
            throw new IllegalArgumentException();
        }
        List<GiftCertificateDTO> certificates = giftCertificateRepository.findAllWithParams(queryParameters)
                .stream()
                .flatMap(Collection::stream)
                .map(certificateMappingService::mapToDto)
                .collect(Collectors.toList());
        return getCertificateDTOSWithTags(certificates);
    }

    @Transactional
    @Override
    public void update(GiftCertificateDTO giftCertificateDTO) {
        Validate.notNull(giftCertificateDTO, "GiftCertificateDTO can't be Null");
        if (giftCertificateDTO.getId() < 1 || giftCertificateDTO.getId() == null) {
            log.error("[GiftCertificateService.update()] An exception occurs: given ID:[{}]" +
                    " can't be less than zero or null", giftCertificateDTO.getId());
            throw new IllegalArgumentException("Given ID can't be less than zero or null");
        }

        GiftCertificate giftCertificate = certificateMappingService.mapFromDto(giftCertificateDTO);
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    giftCertificateRepository.update(giftCertificate);
                    attachAndSaveTags(giftCertificate);
                    log.debug("[GiftCertificateService.update()] GiftCertificate with ID:[{}] updated.",
                            giftCertificateDTO.getId());
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    log.error("[GiftCertificateService.update()] rollback !!!");
                }
            }
        });
    }

    @Override
    public void deleteById(Long id) {
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
        giftCertificateRepository.deleteById(id);
        log.debug("[GiftCertificateService.deleteById()] GiftCertificate for ID:[{}] removed.", id);
    }

    /**This method implements functionality of saving {@link Tag}
     * to into database and attaching Tag to {@link GiftCertificate}
     * during save or update operation.
     * @param giftCertificate value that will be attached to certain Tag in database table
     */
    private void attachAndSaveTags(GiftCertificate giftCertificate) {
        if (!giftCertificate.getTags().isEmpty()) {
            giftCertificate.getTags().forEach(tag -> {
                if (!tagRepository.isExists(tag)) {
                    tagRepository.save(tag);
                }
            });
            giftCertificate.getTags().forEach(tag -> {
                if (tagRepository.findByName(tag.getName()).isPresent()) {
                    Long tagId = tagRepository.findByName(tag.getName()).get().getId();
                    giftCertificateRepository.attachTagToCertificate(tagId, giftCertificate.getId());
                }
            });
        }
    }

    /**This method implements functionality of receiving {@link Tag} objects
     * from database which depends on GiftCertificate#id field
     * @param giftCertificateDTO is used for search Tags by {@link GiftCertificate#getId()}
     * which returns Long datatype field
     * @return List of TagDTOs received from database
     */

    private List<TagDTO> getMappedAndCollected(GiftCertificateDTO giftCertificateDTO) {
        return tagRepository.findAllByCertificate(giftCertificateDTO.getId())
                .stream()
                .flatMap(Collection::stream)
                .map(tagMappingService::mapToDto)
                .collect(Collectors.toList());
    }

    /**This method implements mapping of received from repository
     *  layer {@link Tag} to {@link TagDTO} for each GiftCertificate.
     * @param certificates received from database GiftCertificates
     * @return List of {@link GiftCertificateDTO} with mapped Tags;
     */
    private List<GiftCertificateDTO> getCertificateDTOSWithTags(List<GiftCertificateDTO> certificates) {
        if (certificates.isEmpty()) {
            log.error("[GiftCertificateService.findAll()] GiftCertificates not found");
            throw new GiftCertificateNotFoundException("GiftCertificates not found");
        } else {
            for (GiftCertificateDTO certificateDTO : certificates) {
                List<TagDTO> tagDTOS = getMappedAndCollected(certificateDTO);
                certificateDTO.setTags(new HashSet<>(tagDTOS));
            }
            log.debug("[GiftCertificateService.findAll()] GiftCertificates received from database: [{}]"
                    , certificates);
            return certificates;
        }
    }
}