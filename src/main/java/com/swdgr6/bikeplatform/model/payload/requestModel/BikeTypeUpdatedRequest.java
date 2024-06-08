package com.swdgr6.bikeplatform.model.payload.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BikeTypeUpdatedRequest {
    private String brand;
    private String model;
    private String transmission;
    private String cylinderCapacity;
    private String oilCapacity;
}
