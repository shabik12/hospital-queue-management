package com.hospital.hospital_queue_management.entity;

import com.hospital.hospital_queue_management.enums.MedicineTiming;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "prescription_medicines",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_prescription_medicine",
                        columnNames = {
                                "prescription_id",
                                "medicine_name"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "prescription_id",
            nullable = false
    )
    private Prescription prescription;

    @Column(
            name = "medicine_name",
            nullable = false,
            length = 150
    )
    private String medicineName;

    @Column(
            nullable = false,
            length = 100
    )
    private String dosage;

    @Column(nullable = false)
    private Integer quantity;

    @ElementCollection
    @CollectionTable(
            name = "prescription_medicine_timings",
            joinColumns = @JoinColumn(
                    name = "prescription_medicine_id"
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(
            name = "timing",
            nullable = false
    )
    @Builder.Default
    private Set<MedicineTiming> timings = new HashSet<>();

    @Column(nullable = false)
    private Integer duration;

    @Column(columnDefinition = "TEXT")
    private String instructions;
}