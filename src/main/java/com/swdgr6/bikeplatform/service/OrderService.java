package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    public List<OrderDTO> getAllOrders();
}
