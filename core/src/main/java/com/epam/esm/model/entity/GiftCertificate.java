package com.epam.esm.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "gift_certificate")
@Audited
@AuditTable("gift_certificates_AUD")
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @NotAudited
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "gift_certificate_has_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private Set<Tag> tags = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @NotAudited
    @ManyToMany(mappedBy = "giftCertificates")
    private Set<Receipt> receipts = new HashSet<>();
}