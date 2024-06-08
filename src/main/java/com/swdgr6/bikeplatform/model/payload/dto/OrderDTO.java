package com.swdgr6.bikeplatform.model.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swdgr6.bikeplatform.model.entity.OilProductPackage;
import com.swdgr6.bikeplatform.model.entity.User;
import com.swdgr6.bikeplatform.model.entity.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOrder;

    private Long userId;
    private Long vehicleId;

    private Long oilProductPackage_id;
}
