
package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();

    OrderDto createOrder(OrderDto orderDto);

    OrderDto updateOrder(OrderDto orderDto);

    String deleteOrder(Long id);

    List<OrderDto> searchOrders(String searchTerm);
}

