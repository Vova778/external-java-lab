package com.epam.esm.jwt;

import com.epam.esm.model.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.NotAudited;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String jwt;
    private boolean revoked;
    private boolean expired;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    @NotAudited
    private User user;
}