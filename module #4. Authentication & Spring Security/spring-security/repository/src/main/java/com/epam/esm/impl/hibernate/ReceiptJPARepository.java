package com.epam.esm.impl.hibernate;


import com.epam.esm.ReceiptRepository;
import com.epam.esm.model.entity.Receipt;
import com.epam.esm.utils.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Getter
public class ReceiptJPARepository implements ReceiptRepository {
    private static final String FIND_ALL_BY_TITLE = "SELECT r FROM Receipt r WHERE LOWER(r.title) LIKE LOWER(:title)";
    private static final String FIND_BY_TITLE = "SELECT r FROM Receipt r WHERE r.title= :title";
    private static final String GET_TOTAL_RECORDS = "SELECT COUNT(r.id) from Receipt r";
    private static final String FIND_ALL = "SELECT r FROM Receipt r";
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public boolean isExists(Receipt object) {
        return findById(object.getId()).isPresent();
    }

    @Transactional
    @Override
    public Receipt save(Receipt receipt) {
        log.debug("[ReceiptJPARepository.save()] Receipt with id:[{}] has been saved.", receipt.getId());
        return entityManager.merge(receipt);
    }

    @Override
    public Optional<Receipt> findById(Long id) {
        Receipt receipt = entityManager.find(Receipt.class, id);
        return Optional.ofNullable(receipt);
    }

    @Override
    public List<Receipt> findAll(Pageable pageable) {
        int firstResult = (pageable.getPage() - 1) * pageable.getPageSize();
        return entityManager.createQuery(FIND_ALL, Receipt.class)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Transactional
    @Override
    public Receipt deleteById(Long id) {
        Receipt receipt = entityManager.find(Receipt.class, id);
        log.debug("Receipt for removal {}", receipt);
        entityManager.remove(receipt);
        return receipt;
    }

    @Override
    public Long getTotalRecords() {
        TypedQuery<Long> countQuery = entityManager.createQuery(GET_TOTAL_RECORDS, Long.class);
        return countQuery.getSingleResult();
    }

}