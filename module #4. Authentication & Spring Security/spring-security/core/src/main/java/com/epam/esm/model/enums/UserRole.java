package com.epam.esm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    CUSTOMER(1, "CUSTOMER"),
    ADMIN(2, "ADMIN");

    private final int id;
    private final String roleName;
}
