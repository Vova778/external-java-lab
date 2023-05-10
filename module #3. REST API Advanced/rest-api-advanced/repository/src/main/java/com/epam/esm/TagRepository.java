package com.epam.esm;

import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    boolean isExists(Tag tag);
    Tag save(Tag tag);

    Optional<Tag> findById(Long id);

    Tag deleteById(Long id);

    Optional<Tag> findByName(String name);
    List<Tag> findAllByCertificate(Long certificateId);
}
