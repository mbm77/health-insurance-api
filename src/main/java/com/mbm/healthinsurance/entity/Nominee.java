package com.mbm.healthinsurance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nominees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nominee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nomineeId;

    @ManyToOne
    @JoinColumn(name = "customer_policy_id")
    private CustomerPolicy customerPolicy;

    private String nomineeName;

    private String relationship;

    private LocalDate dateOfBirth;

    private String phone;

    private BigDecimal percentageShare;
}