package com.swdgr6.bikeplatform.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "withdraw_bike_point")
public class WithdrawBikePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdraw_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bike_point_id", nullable = false)
    private BikePoint bikePoint;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Column(nullable = false, length = 50)
    private String status;

    private boolean isDelete = false;

}
