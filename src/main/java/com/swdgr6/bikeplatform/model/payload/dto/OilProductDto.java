package com.swdgr6.bikeplatform.model.payload.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OilProductDto {
    private Long id;
    @NotEmpty(message = "Name should not be empty!")
    private String name;
    @NotEmpty(message = "Description should not be empty!")
    private String description;
    @Positive(message = "Price should be a positive value!")
    private double price;
    @NotEmpty(message = "Technology should not be empty!")
    private String techStand;
    @NotEmpty(message = "Description should not be empty!")
    @Pattern(regexp = "Synthetic|Conventional|Semi-Synthetic", message = "Oil type must be Synthetic, Conventional, or Semi-Synthetic")
    private String oilType;
    private Long brandId;
}
