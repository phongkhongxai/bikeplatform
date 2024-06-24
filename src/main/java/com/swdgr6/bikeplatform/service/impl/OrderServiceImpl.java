package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.OilProductPackage;
import com.swdgr6.bikeplatform.model.entity.Order;
import com.swdgr6.bikeplatform.model.entity.User;
import com.swdgr6.bikeplatform.model.entity.Vehicle;
import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;
import com.swdgr6.bikeplatform.repository.OilProductPackageRepository;
import com.swdgr6.bikeplatform.repository.OrderRepository;
import com.swdgr6.bikeplatform.repository.UserRepository;
import com.swdgr6.bikeplatform.repository.VehicleRepository;
import com.swdgr6.bikeplatform.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private OilProductPackageRepository oilProductPackageRepository;

    public List<OrderDto> getAllOrders() {
        List<Order> list = orderRepository.findAll();
        return mapOrderToDTO(list);
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setDateOrder(LocalDateTime.now());

        // Fetch associated entities
        User user = userRepository.findExistUserById(orderDto.getUserId());
        if(user == null) {
            throw new EntityNotFoundException("User not found with id: " + orderDto.getUserId());
        }
        Vehicle vehicle = vehicleRepository.findExistByVehicle(orderDto.getVehicleId());
        if(vehicle == null) {
            throw new EntityNotFoundException("Vehicle not found with id: " + orderDto.getVehicleId());
        }
        OilProductPackage oilProductPackage = oilProductPackageRepository.findExistByOilProduct(orderDto.getOilProductPackageId());
        if (oilProductPackage == null) {
            throw new EntityNotFoundException("OilProductPackage not found with id: " + orderDto.getOilProductPackageId());
        }

        order.setUser(user);
        order.setVehicle(vehicle);
        order.setOilProductPackage(oilProductPackage);

        orderRepository.save(order);
        return maptoDTO(order);
    }

    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderRepository.findExistOrderById(orderDto.getId());

        // Fetch associated entities
        User user = userRepository.findExistUserById(orderDto.getUserId());
        if(user == null) {
            throw new EntityNotFoundException("User not found with id: " + orderDto.getUserId());
        }
        Vehicle vehicle = vehicleRepository.findExistByVehicle(orderDto.getVehicleId());
        if(vehicle == null) {
            throw new EntityNotFoundException("Vehicle not found with id: " + orderDto.getVehicleId());
        }

        OilProductPackage oilProductPackage = oilProductPackageRepository.findExistByOilProduct(orderDto.getOilProductPackageId());
        if(oilProductPackage == null) {
            throw new EntityNotFoundException("OilProductPackage not found with id: " + orderDto.getOilProductPackageId());
        }

        order.setDateOrder(LocalDateTime.now());
        order.setUser(user);
        order.setVehicle(vehicle);
        order.setOilProductPackage(oilProductPackage);

        orderRepository.save(order);
        return maptoDTO(order);
    }

    public String deleteOrder(Long id) {
        Order order = orderRepository.findExistOrderById(id);
        if(order == null) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        order.setDelete(true);
        orderRepository.save(order);
        return "Order with id: " + id + " was marked as deleted.";
    }

    public List<OrderDto> searchOrders(String searchTerm) {
        Long searchId;
        try {
            searchId = Long.parseLong(searchTerm);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Search term must be a valid order id.");
        }

        return orderRepository.findAll().stream()
                .filter(order -> order.getId().equals(searchId) && !order.isDelete())
                .map(this::maptoDTO)
                .collect(Collectors.toList());
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
