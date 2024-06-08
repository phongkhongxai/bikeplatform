package com.swdgr6.bikeplatform.model.payload.dto;

import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.entity.Order;
import jakarta.persistence.*;
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
    private String name;
    private int changeTimes;
    private int kilometerChange;
    private String description;
    private double totalPrice;
    private Long oilProductId;
}
