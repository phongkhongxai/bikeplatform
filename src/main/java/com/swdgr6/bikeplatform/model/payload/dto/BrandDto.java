package com.swdgr6.bikeplatform.model.payload.dto;

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
public class BrandDto {
    private Long id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Phone number should not be empty!")
    @Pattern(regexp="(^$|[0-9]{8,11})", message = "Phone number must be 8 or 11 digits!")
    private String phone;
    @NotEmpty(message = "Email should not be empty!")
    @Email(regexp = ".+@.+\\..+", message = "Email is invalid!")
    private String email;
    @NotEmpty(message = "Address should not be empty!")
    private String address;
}
