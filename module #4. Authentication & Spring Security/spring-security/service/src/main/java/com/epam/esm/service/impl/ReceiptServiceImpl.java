package com.epam.esm.service.impl;

import com.epam.esm.ReceiptRepository;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.ReceiptDTO;
import com.epam.esm.exception.model.GiftCertificateNotFoundException;
import com.epam.esm.exception.model.ReceiptNotFoundException;
import com.epam.esm.model.entity.Receipt;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ReceiptService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapping.MappingService;
import com.epam.esm.utils.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.service.validator.PageableValidator.checkParams;
import static com.epam.esm.service.validator.PageableValidator.validate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final MappingService<Receipt, ReceiptDTO> mappingService;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;

    @Override
    public ReceiptDTO save(ReceiptDTO receiptDTO) {
        Validate.notNull(receiptDTO,
                "[ReceiptService.save()] ReceiptDTO can't be null!");
        Long userId = receiptDTO.getUserDTO().getId();
        receiptDTO.setUserDTO(userService.findById(userId));
        setGiftCertificatesAndPrice(receiptDTO);
        receiptDTO.setCreateDate(LocalDateTime.now());
        return mappingService.mapToDto(receiptRepository.save(mappingService.mapFromDto(receiptDTO)));
    }

    private void setGiftCertificatesAndPrice(ReceiptDTO receiptDTO) {
        if (!receiptDTO.getGiftCertificates().isEmpty()) {
            Set<GiftCertificateDTO> giftCertificateDTOS = new HashSet<>();

            receiptDTO.getGiftCertificates().forEach(gc -> {
                if (gc.getId() != null) {
                    GiftCertificateDTO giftCertificateDTO = null;
                    try {
                        giftCertificateDTO = giftCertificateService.findById(gc.getId());
                    } catch (GiftCertificateNotFoundException e) {
                        log.error("[ReceiptService.save()] An exception occurs : [{}]", e.getMessage());
                    }
                    if (giftCertificateDTO != null) {
                        giftCertificateDTOS.add(giftCertificateDTO);
                    }
                }
            });
            if (giftCertificateDTOS.isEmpty()) {
                log.error("[ReceiptService.save()] GiftCertificates for new receipt not found");
                throw new GiftCertificateNotFoundException("GiftCertificates for new receipt not found");
            }
            Double price = giftCertificateDTOS.stream()
                    .mapToDouble(GiftCertificateDTO::getPrice)
                    .sum();
            receiptDTO.setPrice(price);
            receiptDTO.setGiftCertificates(giftCertificateDTOS);
        }
    }

    @Override
    public ReceiptDTO findById(Long id) {
        if (id == null || id < 1) {
            log.error("[ReceiptService.findById()] An exception occurs: id:[{}] can't be less than zero or null", id);
            throw new IllegalArgumentException("An exception occurs: Receipt.id can't be less than zero or null");
        }
        ReceiptDTO receiptDTO = receiptRepository.findById(id)
                .map(mappingService::mapToDto)
                .orElseThrow(() -> {
                    log.error("[Receipt.findById()] Receipt for given ID:[{}] not found", id);
                    throw new ReceiptNotFoundException(String.format("Receipt not found (id:[%d])", id));
                });

        log.debug("[ReceiptService.findById()] Receipt received from database: [{}], for ID:[{}]", receiptDTO, id);
        return receiptDTO;
    }

    @Override
    public List<ReceiptDTO> findAll(Pageable pageable) {
        validate(pageable);
        List<ReceiptDTO> receiptDTOS = receiptRepository.findAll(checkParams(pageable, receiptRepository))
                .stream()
                .map(mappingService::mapToDto)
                .toList();
        if (receiptDTOS.isEmpty()) {
            log.error("[ReceiptService.findAll()] Receipts not found");
            throw new ReceiptNotFoundException("Receipts not found");
        }
        log.debug("[ReceiptService.findAll()] Receipts received from database: [{}]", receiptDTOS);
        return receiptDTOS;
    }

    @Override
    public ReceiptDTO deleteById(Long id) {
        if (id == null || id < 1) {
            log.error("[ReceiptService.deleteById()] An exception occurs: id:[{}] can't be less than zero", id);
            throw new IllegalArgumentException("Receipt.id can't be less than zero.");
        }
        Optional<Receipt> receipt = receiptRepository.findById(id);
        log.debug("Delete receipt : {}", receipt);
        if (receipt.isEmpty() || !receiptRepository.isExists(receipt.get())) {
            log.error("[ReceiptService.deleteById()] Receipt with given id:[{}] not found.", id);
            throw new ReceiptNotFoundException(String.format("Receipt with given id:[%d] not found for delete.", id));
        }

        log.debug("[ReceiptService.deleteById()] Receipt for ID:[{}] has been removed", id);
        return mappingService.mapToDto(receiptRepository.deleteById(id));
    }
}