package com.tid.avisExpress.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "validation")
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Instant creation;
    private Instant expire;
    private Instant activation;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private Utilisateur utilisateur;
}
