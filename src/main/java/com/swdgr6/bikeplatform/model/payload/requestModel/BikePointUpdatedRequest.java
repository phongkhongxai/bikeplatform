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
public class BikePointUpdatedRequest {
    private String name;
    private String address;
    @Pattern(regexp="(^$|[0-9]{8,11})", message = "Phone number must be 8 or 11 digits!")
    private String phone;
}
