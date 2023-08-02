package com.epam.esm;

import com.epam.esm.model.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TagRepository
        extends JpaRepository<Tag, Long>{

    boolean existsByName(String name);
    Optional<Tag> findByName(String name);

    @Query("SELECT t FROM GiftCertificate gc JOIN gc.tags t" +
            " WHERE gc.id = (:certificateID) ORDER BY t.id")
    List<Tag> findAllByCertificate(Long certificateID, Pageable pageable);

    @Query("SELECT t FROM Tag t " +
            "JOIN t.giftCertificates gc " +
            "JOIN gc.receipts r " +
            "JOIN r.user u " +
            "WHERE u.id = (SELECT u2.id FROM User u2 " +
            "              JOIN u2.receipts r2 " +
            "              GROUP BY u2.id " +
            "              ORDER BY SUM(r2.price) DESC " +
            "              LIMIT 1) " +
            "GROUP BY t.id, t.name " +
            "ORDER BY COUNT(gc.id) DESC limit 1")
    Optional<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfAllOrders();


    @Query("SELECT COUNT(t.id) from " +
            "GiftCertificate gc JOIN gc.tags t WHERE gc.id = (:giftCertificateID)")
    Long getTotalRecordsForGiftCertificateID(Long giftCertificateID);


}
