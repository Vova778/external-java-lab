package com.epam.esm;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GenericRepository <T, U> {
    boolean isExists(T object);

    T save(T object);

    Optional<T> findByID(U id);

    List<T> findAll(Pageable pageable);

    T deleteByID(U id);

    Long getTotalRecords();
}
