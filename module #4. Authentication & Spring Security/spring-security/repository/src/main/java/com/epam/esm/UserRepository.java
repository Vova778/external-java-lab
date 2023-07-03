package com.epam.esm;

import com.epam.esm.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(:name)")
    List<User> findAllByName(String name, Pageable pageable);

    @Query("SELECT r.user FROM Receipt r WHERE r.id = :receiptID")
    Optional<User> findByReceipt(Long receiptID);

    Long countByFirstNameLikeIgnoreCase(String name);


}
