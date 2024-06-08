package com.swdgr6.bikeplatform.model.payload.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleUpdatedRequest {
    private String plate;
    private String color;
    private Long bikeTypeId;
}
