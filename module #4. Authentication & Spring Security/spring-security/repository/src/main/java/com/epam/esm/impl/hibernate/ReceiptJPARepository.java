package com.epam.esm.impl.hibernate;


import com.epam.esm.ReceiptRepository;
import com.epam.esm.model.entity.Receipt;
import com.epam.esm.utils.PageableValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Getter
public class ReceiptJPARepository implements ReceiptRepository {
    private static final String GET_TOTAL_RECORDS = "SELECT COUNT(r.id) from Receipt r";
    private static final String GET_TOTAL_RECORDS_FOR_USER_ID = "SELECT COUNT(r.id) FROM User u JOIN u.receipts r" +
            " WHERE u.id = (:id)";
    private static final String FIND_ALL = "SELECT r FROM Receipt r";
    private static final String FIND_ALL_BY_USER = "SELECT r FROM User u JOIN" +
            " u.receipts r WHERE u.id = (:id) ORDER BY r.id";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public boolean isExists(Receipt object) {
        return findByID(object.getId()).isPresent();
    }

    @Transactional
    @Override
    public Receipt save(Receipt receipt) {
        return entityManager.merge(receipt);
    }

    @Override
    public Optional<Receipt> findByID(Long id) {
        Receipt receipt = entityManager.find(Receipt.class, id);
        return Optional.ofNullable(receipt);
    }

    @Override
    public List<Receipt> findAll(Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        return entityManager
                .createQuery(FIND_ALL, Receipt.class)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Receipt> findAllByUser(Long userID, Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        return entityManager
                .createQuery(FIND_ALL_BY_USER, Receipt.class)
                .setParameter("id", userID)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Transactional
    @Override
    public Receipt deleteByID(Long id) {
        Receipt receipt = entityManager.find(Receipt.class, id);
        log.debug("Receipt for removal {}", receipt);
        entityManager.remove(receipt);
        return receipt;
    }

    @Override
    public Long getTotalRecords() {
        return entityManager.createQuery(GET_TOTAL_RECORDS, Long.class).getSingleResult();
    }

    @Override
    public Long getTotalRecordsForUserID(Long userID) {
        return entityManager
                .createQuery(GET_TOTAL_RECORDS_FOR_USER_ID, Long.class)
                .setParameter("id", userID)
                .getSingleResult();
    }
}