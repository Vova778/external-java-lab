package com.epam.esm;

import com.epam.esm.utils.Pageable;

import java.util.List;
import java.util.Optional;

public interface GenericRepository <T, U> {
    boolean isExists(T object);

    T save(T object);

    Optional<T> findById(U id);

    List<T> findAll(Pageable pageable);

    T deleteById(U id);

    Long getTotalRecords();
}
