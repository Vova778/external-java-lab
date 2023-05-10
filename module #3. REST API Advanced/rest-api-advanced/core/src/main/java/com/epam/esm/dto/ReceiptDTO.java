package com.epam.esm.dto;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    private Long id;
    private String title;
    private Double price;
    @JsonProperty("createDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate;
    private User user;
    @EqualsAndHashCode.Exclude
    private Set<GiftCertificate> certificates;
}