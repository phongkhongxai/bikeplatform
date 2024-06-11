package com.swdgr6.bikeplatform.model.payload.requestModel;

import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "Manual|Automatic|Semi-Automatic", message = "Transmission must be Manual, Automatic, or Semi-Automatic")
    private String transmission;
    @Pattern(regexp = "^([1-9][0-9]{0,2}|1[0-4][0-9]{2}|1500)cc$", message = "Cylinder capacity must be between 1-1500cc (e.g., 50cc, 100cc, 1350cc)")
    private String cylinderCapacity;
    @Pattern(regexp = "^\\d+(\\.\\d)?L$", message = "Oil capacity must be in the format x.xL (e.g., 1.5L, 0.8L)")
    private String oilCapacity;
}
