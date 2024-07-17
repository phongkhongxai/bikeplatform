package com.swdgr6.bikeplatform.model.payload.dto;

import com.swdgr6.bikeplatform.model.entity.BikePoint;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private Long id;
    private double balance;
    private String accountNumber;
    private String bankName;
    private Long bikePointId;
    private BikePointDto bikePoint;
}
