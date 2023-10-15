package com.epam.esm.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Entity
@Table(name = "tag")
@Audited
@AuditTable("tags_AUD")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    @NotAudited
    private Set<GiftCertificate> giftCertificates = new HashSet<>();
}

