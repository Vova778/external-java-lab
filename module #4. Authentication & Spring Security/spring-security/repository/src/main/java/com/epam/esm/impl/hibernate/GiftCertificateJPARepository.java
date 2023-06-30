package com.epam.esm.impl.hibernate;


import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.exception.model.GiftCertificateNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.utils.PageableValidator;
import com.epam.esm.utils.QueryParameters;
import com.epam.esm.utils.QueryProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private static final String FIND_ALL_BY_RECEIPT = "SELECT gc FROM Receipt r JOIN" +
            " r.giftCertificates gc WHERE r.id = (:id) ORDER BY gc.id";
    private static final String GET_TOTAL_RECORDS = "SELECT COUNT(gc.id) FROM GiftCertificate gc";
    private static final String GET_TOTAL_RECORDS_FOR_RECEIPT_ID = "SELECT COUNT(gc.id) FROM Receipt r JOIN" +
            " r.giftCertificates gc WHERE r.id = (:id)";
    private static final String GET_TOTAL_RECORDS_FOR_NAME_LIKE = "SELECT COUNT(gc.id) FROM GiftCertificate gc WHERE " +
            "LOWER(gc.name) LIKE LOWER(:name)";
    private static final String GET_TOTAL_RECORDS_FOR_TAGS_PARAM = "SELECT COUNT(gc.id) FROM GiftCertificate gc " +
            "JOIN gc.tags t WHERE t.name IN (:tags)";
    private final QueryProvider queryProvider;
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public boolean isExists(GiftCertificate object) {
        boolean result;
        try {
            result = Optional.ofNullable(entityManager
                            .createQuery(IS_EXISTS, GiftCertificate.class)
                            .setParameter("name", object.getName())
                            .getSingleResult())
                    .isPresent();
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
    public List<GiftCertificate> findAllByTags(Set<String> tags, Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        return entityManager
                .createQuery(FIND_ALL_BY_TAGS, GiftCertificate.class)
                .setParameter("tags", tags)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findAllByName(String name, Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        return entityManager
                .createQuery(FIND_ALL_BY_NAME, GiftCertificate.class)
                .setParameter("name", "%" + name + "%")
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> findByID(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public List<GiftCertificate> findAllWithParams(QueryParameters queryParams, Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        queryProvider.setQueryParams(queryParams);
        return entityManager
                .createNativeQuery(queryProvider.findAllWithParams(), GiftCertificate.class)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findAllByReceipt(Long receiptID, Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        return entityManager
                .createQuery(FIND_ALL_BY_RECEIPT, GiftCertificate.class)
                .setParameter("id", receiptID)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findAll(Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        return entityManager
                .createQuery(FIND_ALL, GiftCertificate.class)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Transactional
    @Override
    public GiftCertificate deleteByID(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if (giftCertificate == null) {
            log.error("[GiftCertificateHibernateRepository.deleteById()] Certificate with given id:[{}] not found.", id);
            throw new GiftCertificateNotFoundException(String
                    .format("Certificate with given id:[%d] not found for delete.", id));
        }
        entityManager.remove(giftCertificate);
        return giftCertificate;
    }

    @Override
    public Long getTotalRecords() {
        return entityManager.createQuery(GET_TOTAL_RECORDS, Long.class).getSingleResult();
    }

    @Override
    public Long getTotalRecordsForReceiptID(Long receiptID) {
        return entityManager
                .createQuery(GET_TOTAL_RECORDS_FOR_RECEIPT_ID, Long.class)
                .setParameter("id", receiptID)
                .getSingleResult();
    }

    public Long getTotalRecordsForNameLike(String name) {
        return entityManager
                .createQuery(GET_TOTAL_RECORDS_FOR_NAME_LIKE, Long.class)
                .setParameter("name", "%" + name + "%")
                .getSingleResult();
    }

    public Long getTotalRecordsForParams(QueryParameters queryParams) {
        queryProvider.setQueryParams(queryParams);
        return (Long) entityManager
                .createNativeQuery(queryProvider.getTotalRecordsForParams(), Long.class)
                .getSingleResult();
    }

    public Long getTotalRecordsForTagsParam(Set<String> tagNames) {
        return entityManager
                .createQuery(GET_TOTAL_RECORDS_FOR_TAGS_PARAM, Long.class)
                .setParameter("tags", tagNames)
                .getSingleResult();
    }
}