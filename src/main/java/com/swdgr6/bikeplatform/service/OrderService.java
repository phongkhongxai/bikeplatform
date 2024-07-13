
package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.entity.Order;
import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrdersResponse;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface OrderService {

    OrdersResponse getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderById(Long id);

    OrderDto updateVehicleForOrder(Long id,Long vehicleId);
    OrderDto updateStatusOfOrder(Long id);
    OrderDto updateChangeTimeOfOrder(Long id);

    OrdersResponse getAllOrdersByUser(Long uid,int pageNo, int pageSize, String sortBy, String sortDir);

//    void updateWhenDate();

    String deleteOrder(Long id);

    List<OrderDto> searchOrders(String searchTerm);
    Long countAvailableOrder();
}

