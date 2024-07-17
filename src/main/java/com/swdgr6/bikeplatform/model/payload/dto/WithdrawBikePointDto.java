package com.swdgr6.bikeplatform.model.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawBikePointDto {
    private Long id;
    private Long bikePointId;
    private double amount;
    private LocalDateTime date;
    private String status;
    private BikePointDto bikePoint;
}
