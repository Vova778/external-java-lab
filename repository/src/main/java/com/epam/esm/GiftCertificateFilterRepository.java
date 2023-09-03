package com.epam.esm;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.utils.QueryParameters;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GiftCertificateFilterRepository {
    List<GiftCertificate> findAllWithParams(Pageable pageable, QueryParameters queryParameters);
}
