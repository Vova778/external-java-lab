package com.epam.esm;

import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository
        extends GenericRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    List<Tag> findAllByCertificate(Long certificateId);
}
