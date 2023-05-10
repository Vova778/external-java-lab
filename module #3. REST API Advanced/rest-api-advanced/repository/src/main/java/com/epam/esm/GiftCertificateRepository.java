package com.epam.esm;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.utils.QueryParameters;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    boolean isExists(GiftCertificate certificate);

    GiftCertificate save(GiftCertificate certificate);

    Optional<GiftCertificate> findById(Long id);

    List<GiftCertificate> findAllByName(String name);

    List<GiftCertificate> findAllWithParams(QueryParameters queryParameters);

    List<GiftCertificate> findAll();

    GiftCertificate deleteById(Long id);

    void attachTagToCertificate(Long tagId, Long certificateId);

    void update(GiftCertificate giftCertificate);

}
