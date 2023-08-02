package com.epam.esm.service.mapping.impl;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.mapping.MappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GiftCertificateMappingServiceImpl implements MappingService<GiftCertificate, GiftCertificateDTO> {
    private final MappingService<Tag, TagDTO> tagMappingService;
    @Override
    public GiftCertificate mapFromDto(GiftCertificateDTO certificateDTO) {
        GiftCertificate model = new GiftCertificate();
        BeanUtils.copyProperties(certificateDTO, model);
        if (certificateDTO.getTags() != null && !certificateDTO.getTags().isEmpty()) {
            certificateDTO.getTags().forEach(tagDTO -> model.getTags().add(tagMappingService.mapFromDto(tagDTO)));
        }
        log.debug("[GiftCertificateMappingService] GiftCertificateDTO converted to GiftCertificate model: [{}]", model);
        return model;
    }

    @Override
    public GiftCertificateDTO mapToDto(GiftCertificate model) {
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        BeanUtils.copyProperties(model, giftCertificateDTO);
        if (model.getTags() != null && !model.getTags().isEmpty()) {
            model.getTags().forEach(tag -> giftCertificateDTO.getTags().add(tagMappingService.mapToDto(tag)));
        }
        log.debug("[GiftCertificateMappingService.mapToDTO()] Model converted to DTO: [{}]", giftCertificateDTO);
        return giftCertificateDTO;
    }
}