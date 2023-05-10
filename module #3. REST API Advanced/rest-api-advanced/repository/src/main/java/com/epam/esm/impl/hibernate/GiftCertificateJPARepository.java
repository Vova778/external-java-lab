package com.epam.esm.impl.hibernate;


import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.utils.QueryParameters;
import com.epam.esm.utils.QueryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GiftCertificateJPARepository implements GiftCertificateRepository {
    private static final String IS_EXISTS = "SELECT gc FROM GiftCertificate gc WHERE LOWER(gc.name) = (:name)";
    private static final String FIND_ALL_BY_NAME =
            "SELECT gc FROM GiftCertificate gc WHERE LOWER(gc.name) LIKE LOWER(:name) ORDER BY gc.id";
    private static final String FIND_ALL =
            "SELECT gc FROM GiftCertificate gc ORDER BY gc.id";
    private static final String FIND_ALL_BY_TAGS = "SELECT DISTINCT gc FROM GiftCertificate gc JOIN gc.tags t" +
            " WHERE t.name IN (:tags) ORDER BY gc.id";
    private static final String GET_TOTAL_RECORDS = "SELECT COUNT(gc.id) from GiftCertificate gc";
    private final QueryProvider queryProvider;
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public boolean isExists(GiftCertificate object) {
        boolean result;
        try {
            TypedQuery<GiftCertificate> query = entityManager
                    .createQuery(IS_EXISTS, GiftCertificate.class);
            query.setParameter("name", object.getName());
            result = Optional.ofNullable(query.getSingleResult()).isPresent();
        } catch (NoResultException e) {
            return false;
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        log.debug("[GiftCertificateHibernateRepository.save()] GiftCertificate :[{}] has been saved.",
                giftCertificate);
        return entityManager.merge(giftCertificate);
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public List<GiftCertificate> findAllByName(String name) {
        return null;
    }

    @Override
    public List<GiftCertificate> findAllWithParams(QueryParameters queryParameters) {
        return null;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return null;
    }

    @Transactional
    @Override
    public GiftCertificate deleteById(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        entityManager.remove(giftCertificate);
        return giftCertificate;
    }

    @Override
    public void attachTagToCertificate(Long tagId, Long certificateId) {

    }

    @Override
    public void update(GiftCertificate giftCertificate) {

    }

}