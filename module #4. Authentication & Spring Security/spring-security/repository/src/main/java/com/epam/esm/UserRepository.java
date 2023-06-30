package com.epam.esm;

import com.epam.esm.model.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends  GenericRepository<User, Long> {
    boolean isExistsByEmail(User user);
    Optional<User> findByEmail(String email);

    List<User> findAllByName(String name, Pageable pageable);

    Optional<User> findByReceipt(Long receiptID);

    Long getTotalRecordsForNameLike(String name);

}
