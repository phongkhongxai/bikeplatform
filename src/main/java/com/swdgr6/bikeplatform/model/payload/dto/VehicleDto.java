package com.swdgr6.bikeplatform.model.payload.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[0-9]{2}[A-Z][0-9]{5,6}$", message = "Plate format should be xxXxxxxxx (e.g: 79N305349)")
    private String plate;
    @NotEmpty(message = "Color should not be empty!")
    private String color;
    @NotNull(message = "User ID should not be null!")
    private Long userId;
    @NotNull(message = "User ID should not be null!")
    private Long bikeTypeId;
}
