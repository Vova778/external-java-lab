package com.epam.esm;

import com.epam.esm.model.User;
import com.epam.esm.utils.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends  GenericRepository<User, Long> {
    List<User> findAllByName(String name, Pageable pageable);

    Optional<User> findByName(String name);
}
