package com.swdgr6.bikeplatform.model.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swdgr6.bikeplatform.model.entity.OrderUsing;
import com.swdgr6.bikeplatform.model.entity.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private Long walletId;
    private Long orderUsingId;
    private double payAmount;
    private String status;
    private LocalDateTime date;
}
