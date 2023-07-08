package com.epam.esm.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

@Slf4j
public class PageableValidator {
    private PageableValidator() {
    }


    public static int getLastPage(Pageable pageable, Long totalRecords) {
        if (totalRecords == null || totalRecords <= 0) {
            log.error("[PageableValidator.getLastPage()] No records found...");
            throw new IllegalArgumentException("[PageableValidator.getLastPage()] No records found...");
        }
        if (totalRecords % pageable.getPageSize() == 0) {
            return (int) (totalRecords / pageable.getPageSize());
        } else {
            return (int) (totalRecords / pageable.getPageSize()) + 1;
        }
    }

    public static int getFirstResultValue(Pageable pageable) {
        if (pageable == null) {
            log.error("[PageableValidator.getFirstResultValue()] Pageable can not be null");
            throw new IllegalArgumentException("[PageableValidator.getFirstResultValue()] Pageable can not be null");
        }
        return pageable.getPageNumber() * pageable.getPageSize();
    }
}
