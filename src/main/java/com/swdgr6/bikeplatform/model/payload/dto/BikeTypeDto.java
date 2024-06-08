package com.swdgr6.bikeplatform.model.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BikeTypeDto {
    private Long id;
    private String brand;
    private String model;
    private String transmission;
    private String cylinder_capacity;
    private String oil_capacity;
}
