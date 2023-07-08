package com.epam.esm.dto;

import com.epam.esm.model.entity.Receipt;
import com.epam.esm.model.enums.UserRole;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    private UserRole userRole;

    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Receipt> receipts = new HashSet<>();
}