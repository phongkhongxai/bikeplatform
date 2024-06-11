package com.swdgr6.bikeplatform.model.payload.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BikePointUpdatedRequest {
    private String name;
    private String address;
    private String phone;
}
