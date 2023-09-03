package com.epam.esm.impl;

import com.epam.esm.GiftCertificateFilterRepository;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.utils.QueryParameters;
import com.epam.esm.utils.QueryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateFilterRepository {
    private final QueryProvider queryProvider;
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<GiftCertificate> findAllWithParams(Pageable pageable, QueryParameters queryParams) {
        int firstResult = getFirstResultValue(pageable);

        queryProvider.setQueryParams(queryParams);
        return entityManager
                .createNativeQuery(queryProvider.findAllWithParams(), GiftCertificate.class)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private static int getFirstResultValue(Pageable pageable) {
        if (pageable == null) {
            log.error("[PageableValidator.getFirstResultValue()] Pageable can not be null");
            throw new IllegalArgumentException("[PageableValidator.getFirstResultValue()] Pageable can not be null");
        }
        return pageable.getPageNumber() * pageable.getPageSize();
    }

}
