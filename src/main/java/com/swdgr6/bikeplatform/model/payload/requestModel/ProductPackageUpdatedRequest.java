package com.swdgr6.bikeplatform.model.payload.requestModel;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackageUpdatedRequest {
    private String name;
    @Positive(message = "Change times should be a positive integer!")
    private int changeTimes;
    @Positive(message = "Kilometer change should be a positive integer!")
    private int kilometerChange;
    private String description;
    @Positive(message = "Total price should be a positive value!")
    private double totalPrice;
}
