package com.epam.esm;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.utils.Pageable;
import com.epam.esm.utils.QueryParameters;

import java.util.List;
import java.util.Set;

public interface GiftCertificateRepository
        extends GenericRepository<GiftCertificate, Long> {

    List<GiftCertificate> findAllByTags(Set<String> tags,
                                        Pageable pageable);
    List<GiftCertificate> findAllByName(String name,
                                        Pageable pageable);
    List<GiftCertificate> findAllWithParams(QueryParameters queryParams,
                                            Pageable pageable);

}
