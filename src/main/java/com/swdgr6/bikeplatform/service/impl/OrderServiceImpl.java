package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.Order;
import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;
import com.swdgr6.bikeplatform.repository.OrderRepository;
import com.swdgr6.bikeplatform.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<OrderDto> getAllOrders() {
        List<Order> list = orderRepository.findAll();
        return mapOrderToDTO(list);
    }

    private OrderDto maptoDTO(Order order){
        OrderDto orderDTO = new OrderDto();
        orderDTO.setId(order.getId());
        orderDTO.setDateOrder(order.getDateOrder());
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setVehicleId(order.getVehicle().getId());
        orderDTO.setOilProductPackageId(order.getOilProductPackage().getId());
        return orderDTO;
    }

    private List<OrderDto> mapOrderToDTO(List<Order> list){
        return list.stream().map(this::maptoDTO).collect(Collectors.toList());
    }
}
