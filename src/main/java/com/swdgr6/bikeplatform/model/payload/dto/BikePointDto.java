package com.swdgr6.bikeplatform.model.payload.dto;


import com.swdgr6.bikeplatform.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BikePointDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private Long walletId;
    private Long userId;
}
