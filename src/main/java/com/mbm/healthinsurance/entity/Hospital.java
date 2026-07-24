package com.mbm.healthinsurance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalId;

    private String hospitalName;

    private String registrationNumber;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String networkType;

    private String status;
}