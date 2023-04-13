package com.epam.esm;

import com.epam.esm.model.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    boolean isExists(GiftCertificate certificate);
    Long save(GiftCertificate certificate);

    Optional<GiftCertificate> findById(Long id);

    Optional<List<GiftCertificate>> findAllByName(String name);

    Optional<List<GiftCertificate>> findAll();

    void deleteById(Long id);
    public void attachTagToCertificate(Long tagId, Long certificateId);
    void update(Long id, GiftCertificate giftCertificate);
}
