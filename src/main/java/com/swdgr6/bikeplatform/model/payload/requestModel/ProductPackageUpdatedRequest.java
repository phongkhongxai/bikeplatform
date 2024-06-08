package com.swdgr6.bikeplatform.model.payload.requestModel;

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
    private int changeTimes;
    private int kilometerChange;
    private String description;
    private double totalPrice;
}
