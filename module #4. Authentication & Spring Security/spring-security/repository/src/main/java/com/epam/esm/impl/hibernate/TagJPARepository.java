package com.epam.esm.impl.hibernate;

import com.epam.esm.TagRepository;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.utils.PageableValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
public class TagJPARepository implements TagRepository {
    private static final String FIND_SINGLE_BY_NAME = "SELECT t FROM Tag t WHERE t.name = (:name)";
    private static final String GET_TOTAL_RECORDS = "SELECT COUNT(t.id) from Tag t";
    private static final String GET_TOTAL_RECORDS_FOR_CERTIFICATE_ID = "SELECT COUNT(t.id) from " +
            "GiftCertificate gc JOIN gc.tags t WHERE gc.id = (:id)";
    private static final String FIND_ALL = "SELECT t FROM Tag t ORDER BY t.id";
    private static final String FIND_ALL_BY_CERTIFICATE = "SELECT t FROM GiftCertificate gc" +
            " JOIN gc.tags t WHERE gc.id = (:id) ORDER BY t.id";


    //TODO check this brute forced query
    private static final String FIND_MOST_WIDELY_USED_TAG_OF_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS =
            "SELECT tag.id, tag.name FROM external_lab.tag" +
                    "            JOIN external_lab.gift_certificate_has_tag gcht ON tag.id = gcht.tag_id AND gcht.gift_certificate_id IN" +
                    "            (SELECT gift_certificate.id FROM external_lab.gift_certificate" +
                    "            JOIN external_lab.receipt_has_gift_certificate rhgc ON gift_certificate.id = rhgc.gift_certificate_id" +
                    "            JOIN external_lab.receipt ON gift_certificate.id = rhgc.gift_certificate_id AND receipt.user_id = " +
                    "            (SELECT users.id FROM external_lab.users" +
                    "            JOIN external_lab.receipt ON users.id = receipt.user_id" +
                    "            JOIN external_lab.gift_certificate gc ON gc.id = rhgc.gift_certificate_id" +
                    "            GROUP BY users.id" +
                    "            ORDER BY SUM(receipt.price) DESC LIMIT 1))" +
                    "            GROUP BY gcht.tag_id" +
                    "            ORDER BY COUNT(gcht.gift_certificate_id) DESC";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isExists(Tag object) {
        return findByName(object.getName()).isPresent();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        log.debug("[TagJPARepository.save()] Tag with id:[{}] has been saved.", tag.getId());
        return tag;
    }

    @Override
    public Optional<Tag> findByID(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Optional<Tag> result;
        try {
            result = Optional.ofNullable(entityManager
                    .createQuery(FIND_SINGLE_BY_NAME, Tag.class)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.error("[TagJPARepository.findByName()] NoResultException, Optional.empty() returned!!!");
            return Optional.empty();
        }
        return result;
    }

    @Override
    public List<Tag> findAll(Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        return entityManager
                .createQuery(FIND_ALL, Tag.class)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Tag> findAllByCertificate(Long certificateID, Pageable pageable) {
        int firstResult = PageableValidator.getFirstResultValue(pageable);
        return entityManager
                .createQuery(FIND_ALL_BY_CERTIFICATE, Tag.class)
                .setParameter("id", certificateID)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Optional<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfAllReceipts() {
        Optional<Tag> result;
        try {
            result = Optional.ofNullable((Tag) entityManager
                    .createNativeQuery(FIND_MOST_WIDELY_USED_TAG_OF_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS, Tag.class)
                    .setMaxResults(1)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.error("[TagJPARepository.findMostWidelyUsedTagOfUserWithHighestCostOfAllReceipts()]" +
                    " NoResultException, Optional.empty() returned!!!");
            return Optional.empty();
        }
        return result;
    }

    @Transactional
    @Override
    public Tag deleteByID(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);
        return tag;
    }

    @Override
    public Long getTotalRecords() {
        return entityManager.createQuery(GET_TOTAL_RECORDS, Long.class).getSingleResult();
    }

    @Override
    public Long getTotalRecordsForGiftCertificateID(Long giftCertificateID) {
        return entityManager
                .createQuery(GET_TOTAL_RECORDS_FOR_CERTIFICATE_ID, Long.class)
                .setParameter("id", giftCertificateID)
                .getSingleResult();
    }
}