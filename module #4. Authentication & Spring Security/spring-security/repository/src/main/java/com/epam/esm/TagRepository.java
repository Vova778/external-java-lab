package com.epam.esm;

import com.epam.esm.model.entity.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TagRepository
        extends GenericRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    List<Tag> findAllByCertificate(Long certificateID, Pageable pageable);

    Optional<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfAllReceipts();

    Long getTotalRecordsForGiftCertificateID(Long giftCertificateID);
}
