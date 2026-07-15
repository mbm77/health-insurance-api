package com.mbm.healthinsurance.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "claim_id")
    private Claim claim;

    private String paymentReference;

    private String paymentMethod;

    private BigDecimal approvedAmount;

    private LocalDate paymentDate;

    private String paymentStatus;

    private String transactionId;
}