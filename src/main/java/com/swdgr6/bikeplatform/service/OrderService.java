
package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;

import java.util.List;

public interface OrderService {
    public List<OrderDto> getAllOrders();
}

