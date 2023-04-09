package com.epam.esm.model;

import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GiftCertificate {
    private Long id;

    @NotBlank
    @NonNull
    private String name;

    @NotBlank
    @NonNull
    private String description;

    @NonNull
    private Double price;

    @NonNull
    private Integer duration;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
