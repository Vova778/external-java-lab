package com.epam.esm;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface GiftCertificateRepository
        extends JpaRepository<GiftCertificate, Long> {

    @Query("SELECT DISTINCT gc FROM GiftCertificate gc JOIN gc.tags t" +
            " WHERE t.name IN (:tags) ORDER BY gc.id")
    List<GiftCertificate> findAllByTags(Set<String> tags, Pageable pageable);

    List<GiftCertificate> findAllByNameContainingIgnoreCase(String name, Pageable pageable);


    @Query("SELECT gc FROM Receipt r JOIN" +
            " r.giftCertificates gc WHERE r.id = (:receiptID) ORDER BY gc.id")
    List<GiftCertificate> findAllByReceipt(Long receiptID, Pageable pageable);

    @Query("SELECT COUNT(gc.id) FROM Receipt r JOIN" +
            " r.giftCertificates gc WHERE r.id = (:receiptID)")
    Long getTotalRecordsForReceiptID(Long receiptID);

    Long countByNameContainingIgnoreCase(String name);

    @Query( "SELECT COUNT(gc.id) FROM GiftCertificate gc " +
            "JOIN gc.tags t WHERE t.name IN (:tagNames)")
    Long getTotalRecordsForTagsParam(Set<String> tagNames);

    boolean existsByName(String name);
}
