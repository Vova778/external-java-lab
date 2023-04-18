package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;

import java.util.List;

public interface GiftCertificateService {
    void save(GiftCertificateDTO giftCertificate);

    GiftCertificateDTO findById(Long id);

    List<GiftCertificateDTO> findAll();
    List<GiftCertificateDTO> findAllByName(String name);

    void update(GiftCertificateDTO giftCertificateDTO);

    void deleteById(Long id);
}
