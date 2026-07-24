package com.mbm.healthinsurance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 500)
    private String refreshToken;

    private LocalDateTime expiryDate;

    private Boolean revoked;

    private LocalDateTime createdAt;
}