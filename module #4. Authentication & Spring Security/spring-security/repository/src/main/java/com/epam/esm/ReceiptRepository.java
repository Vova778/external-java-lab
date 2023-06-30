package com.epam.esm;

import com.epam.esm.model.entity.Receipt;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReceiptRepository extends GenericRepository<Receipt, Long>{
    List<Receipt> findAllByUser(Long userID, Pageable pageable);

    Long getTotalRecordsForUserID(Long userID);
}
