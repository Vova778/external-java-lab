package com.epam.esm;

import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    boolean isExists(Tag tag);
    Long save(Tag tag);

    Optional<Tag> findById(Long id);

    Optional<List<Tag>> findAllByName(String name);

    Optional<List<Tag>> findAll();

    void deleteById(Long id);

    Optional<Tag> findByName(String name);
    Optional<List<Tag>> findAllByCertificate(Long certificateId);
}
