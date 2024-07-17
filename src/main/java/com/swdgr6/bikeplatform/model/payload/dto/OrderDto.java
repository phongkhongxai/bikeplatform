package com.swdgr6.bikeplatform.model.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDateTime dateOrder;
    private int changeTimes;
    private String status;
    private Long userId;
    private Long vehicleId;
    private Long oilProductPackageId;
    private OilProductPackageDto oilProductPackage;
    private VehicleDto vehicle;
}
