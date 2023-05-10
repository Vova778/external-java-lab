package com.epam.esm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private Double price;
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "receipt_has_gift_certificate",
            joinColumns = @JoinColumn(name = "receipt_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id")
    )
    private Set<GiftCertificate> giftCertificates = new HashSet<>();
}