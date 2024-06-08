package com.swdgr6.bikeplatform.model.payload.requestModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandUpdatedRequest {
    private String name;
    @Pattern(regexp="(^$|[0-9]{8,11})", message = "Phone number must be 8 or 11 digits!")
    private String phone;
    @Email(regexp = ".+@.+\\..+", message = "Email is invalid!")
    private String email;
    private String address;
}
