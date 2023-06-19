package com.epam.esm.service;


import com.epam.esm.utils.Pageable;

import java.util.List;

public interface GenericService<T, U> {
    T save(T object);

    T findById(U id);

    List<T> findAll(Pageable pageable);

    T deleteById(U id);
}