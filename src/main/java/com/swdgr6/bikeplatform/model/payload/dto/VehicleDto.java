package com.swdgr6.bikeplatform.model.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;
    private String plate;
    private String color;
    private Long userId;
    private Long bikeTypeId;
}
