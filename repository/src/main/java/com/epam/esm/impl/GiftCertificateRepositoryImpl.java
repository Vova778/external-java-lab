package com.epam.esm.impl;

import com.epam.esm.GiftCertificateFilterRepository;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.utils.QueryParameters;
import com.epam.esm.utils.QueryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateFilterRepository {
    private final QueryProvider queryProvider;
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<GiftCertificate> findAllWithParams( QueryParameters queryParams) {

        queryProvider.setQueryParams(queryParams);
        return entityManager
                .createNativeQuery(queryProvider.findAllWithParams(),
                        GiftCertificate.class)
                .getResultList();
    }



}
