package com.epam.esm;

import com.epam.esm.model.entity.Receipt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findAllByUser(Long userID, Pageable pageable);

    @Query("SELECT COUNT(r.id) FROM User u JOIN u.receipts r" +
            " WHERE u.id = (:userID)")
    Long countReceiptByUser(Long userID);
}
