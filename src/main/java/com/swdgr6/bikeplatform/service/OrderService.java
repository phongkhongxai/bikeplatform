
package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();

    OrderDTO createOrder(OrderDTO orderDto);

    OrderDTO updateOrder(OrderDTO orderDto);

    String deleteOrder(Long id);

    List<OrderDTO> searchOrders(String searchTerm);
}

