package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificateDTO {
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer duration;

    @JsonProperty("createDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate;

    @JsonProperty("lastUpdateDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastUpdateDate;
    @EqualsAndHashCode.Exclude
    private Set<TagDTO> tags;
}
