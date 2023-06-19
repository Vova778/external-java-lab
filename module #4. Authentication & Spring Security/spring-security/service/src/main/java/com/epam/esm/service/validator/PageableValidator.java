package com.epam.esm.service.validator;


import com.epam.esm.GenericRepository;
import com.epam.esm.utils.Pageable;
import org.apache.commons.lang3.Validate;

public class PageableValidator {
    private PageableValidator() {
    }

    public static void validate(Pageable pageable) {
        Validate.notNull(pageable, "Pageable can't be null!");
        Validate.notNull(pageable.getPage(), "Pageable.page can't be null!");
        Validate.notNull(pageable.getPageSize(), "Pageable.pageSize can't be null!");
    }

    public static <T, U> Pageable checkParams(Pageable pageable, GenericRepository<T, U> repository) {
        Long totalRecords = repository.getTotalRecords();
        int lastPage = getLastPage(pageable, totalRecords);
        if (pageable.getPage() < 1) {
            pageable.setPage(1);
        }
        if (pageable.getPage() > getLastPage(pageable, totalRecords)) {
            pageable.setPage(lastPage);
        }
        return pageable;
    }


    private static int getLastPage(Pageable pageable, Long totalRecords) {
        if (totalRecords % pageable.getPageSize() == 0) {
            return (int) (totalRecords / pageable.getPageSize());
        } else {
            return (int) (totalRecords / pageable.getPageSize()) + 1;
        }
    }
}
