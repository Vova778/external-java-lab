package com.epam.esm.service.mapping.impl;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.ReceiptDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Receipt;
import com.epam.esm.model.User;
import com.epam.esm.service.mapping.MappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiptMappingServiceImpl implements MappingService<Receipt, ReceiptDTO> {
    private final MappingService<GiftCertificate, GiftCertificateDTO> certificateMappingService;
    private final MappingService<User, UserDTO> userMappingService;

    @Override
    public Receipt mapFromDto(ReceiptDTO receiptDTO) {
        Receipt model = new Receipt();
        BeanUtils.copyProperties(receiptDTO, model);
        if (receiptDTO.getGiftCertificates() != null && !receiptDTO.getGiftCertificates().isEmpty()) {
            receiptDTO.getGiftCertificates().forEach(certificateDTO ->
                    model.getGiftCertificates().add(certificateMappingService.mapFromDto(certificateDTO)));
        }
        if (receiptDTO.getUserDTO() != null) {
            model.setUser(userMappingService.mapFromDto(receiptDTO.getUserDTO()));
        }
        log.debug("[ReceiptMappingService] ReceiptDTO converted to Receipt model: [{}]", model);
        return model;
    }

    @Override
    public ReceiptDTO mapToDto(Receipt model) {
        ReceiptDTO receiptDTO = new ReceiptDTO();
        BeanUtils.copyProperties(model, receiptDTO);
        if (model.getGiftCertificates() != null && !model.getGiftCertificates().isEmpty()) {
            model.getGiftCertificates().forEach(giftCertificate ->
                    receiptDTO.getGiftCertificates().add(certificateMappingService.mapToDto(giftCertificate)));
        }
        if (model.getUser() != null) {
            receiptDTO.setUserDTO(userMappingService.mapToDto(model.getUser()));
        }
        log.debug("[ReceiptMappingService] Receipt model converted to ReceiptDTO: [{}]", receiptDTO);
        return receiptDTO;
    }
}