package com.hospital.hospital_queue_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dispensings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dispensing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "prescription_id",
            nullable = false,
            unique = true
    )
    private Prescription prescription;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dispensedAt;

    @OneToMany(
            mappedBy = "dispensing",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<DispensingItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (dispensedAt == null) {
            dispensedAt = LocalDateTime.now();
        }
    }
}