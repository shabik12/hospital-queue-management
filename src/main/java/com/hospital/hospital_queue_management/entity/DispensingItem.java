package com.hospital.hospital_queue_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dispensing_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispensingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "dispensing_id",
            nullable = false
    )
    private Dispensing dispensing;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "medicine_id",
            nullable = false
    )
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "medicine_batch_id",
            nullable = false
    )
    private MedicineBatch medicineBatch;

    @Column(nullable = false)
    private Integer quantity;
}