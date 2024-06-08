package com.swdgr6.bikeplatform.model.payload.dto;

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
    private String name;
    private String description;
    private double price;
    private String techStand;
    private String oilType;
    private Long brandId;
}
