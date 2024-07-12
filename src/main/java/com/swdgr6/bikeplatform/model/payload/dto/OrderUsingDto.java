package com.swdgr6.bikeplatform.model.payload.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUsingDto {
    private Long id;
    private double price;
    private int rating;
    private String feedback;
    private String status;
    private LocalDate dateUsing;
    private LocalDateTime dateUpdateUsing;
    private Long orderId;
    private Long bikePointId;
}
