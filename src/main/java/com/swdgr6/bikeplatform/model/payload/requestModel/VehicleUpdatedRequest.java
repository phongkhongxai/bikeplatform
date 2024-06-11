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
public class VehicleUpdatedRequest {
    @Pattern(regexp = "^[0-9]{2}[A-Z][0-9]{5,6}$", message = "Plate format should be xxXxxxxxx (e.g: 79N305349)")
    private String plate;
    private String color;
    private Long bikeTypeId;
}
