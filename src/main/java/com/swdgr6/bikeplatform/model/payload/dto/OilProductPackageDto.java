package com.swdgr6.bikeplatform.model.payload.dto;

import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OilProductPackageDto {
    private Long id;
    @NotEmpty(message = "Name should not be empty!")
    private String name;

    @Positive(message = "Change times should be a positive integer!")
    private int changeTimes;

    @Positive(message = "Kilometer change should be a positive integer!")
    private int kilometerChange;

    @NotEmpty(message = "Description should not be empty!")
    private String description;

    @Positive(message = "Total price should be a positive value!")
    private double totalPrice;

    @NotNull(message = "Oil product ID should not be null!")
    private Long oilProductId;
    private OilProductDto oilProduct;
}
