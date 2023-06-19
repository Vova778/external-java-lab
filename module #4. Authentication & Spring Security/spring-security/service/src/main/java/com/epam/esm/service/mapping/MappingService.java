package com.epam.esm.service.mapping;

public interface MappingService <T, U> {
    T mapFromDto(U dto);

    U mapToDto(T model);
}