package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificateDTO {
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

    @JsonProperty("createDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate;

    @JsonProperty("lastUpdateDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastUpdateDate;

}
