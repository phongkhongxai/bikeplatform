package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.*;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.OilProductDto;
import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrdersResponse;
import com.swdgr6.bikeplatform.repository.OilProductPackageRepository;
import com.swdgr6.bikeplatform.repository.OrderRepository;
import com.swdgr6.bikeplatform.repository.UserRepository;
import com.swdgr6.bikeplatform.repository.VehicleRepository;
import com.swdgr6.bikeplatform.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

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

    @Autowired
    private ModelMapper modelMapper;

    public OrdersResponse getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Order> orders = orderRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<Order> listOfOrders = orders.getContent();

        List<OrderDto> content = listOfOrders.stream().map(op -> modelMapper.map(op, OrderDto.class)).collect(Collectors.toList());

        OrdersResponse templatesResponse = new OrdersResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orders.getNumber());
        templatesResponse.setPageSize(orders.getSize());
        templatesResponse.setTotalElements(orders.getTotalElements());
        templatesResponse.setTotalPages(orders.getTotalPages());
        templatesResponse.setLast(orders.isLast());

        return templatesResponse;
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        ZonedDateTime zonedDateTime = ZonedDateTime.now( ZoneId.of("UTC+07:00"));
        order.setDateOrder(zonedDateTime.toLocalDateTime());

        // Fetch associated entities
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + orderDto.getUserId()));

        Vehicle vehicle = vehicleRepository.findById(orderDto.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + orderDto.getVehicleId()));

        OilProductPackage oilProductPackage = oilProductPackageRepository.findById(orderDto.getOilProductPackageId())
                .orElseThrow(() -> new EntityNotFoundException("OilProductPackage not found with id: " + orderDto.getOilProductPackageId()));

        if (!user.getVehicles().contains(vehicle)){
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Vehicle not valid.(user not have this bike)");
        }
        if(!oilProductPackage.getOilProduct().getBikeTypes().contains(vehicle.getBikeType())){
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Vehicle not valid.(product not suit with bike type)");
        }
        order.setUser(user);
        order.setVehicle(vehicle);
        order.setOilProductPackage(oilProductPackage);
        order.setStatus("Available");
        order.setChangeTimes(oilProductPackage.getChangeTimes());
        order.setDelete(false);
        return modelMapper.map(orderRepository.save(order), OrderDto.class);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id);
        }
        return modelMapper.map(orderOptional.get(), OrderDto.class);
    }

    public OrderDto updateVehicleForOrder(Long id, Long vehicleId) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id);
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Vehicle not found with ID: " + vehicleId));

        Order existingOrder = orderOptional.get();
        if (!existingOrder.getUser().getVehicles().contains(vehicle)){
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Vehicle not valid.");
        }
        if(!existingOrder.getOilProductPackage().getOilProduct().getBikeTypes().contains(vehicle.getBikeType())){
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Vehicle not valid.(product not suit with bike type)");
        }
        existingOrder.setVehicle(vehicle);
        return modelMapper.map(orderRepository.save(existingOrder),OrderDto.class);
    }

    @Override
    public OrderDto updateStatusOfOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id);
        }
        Order existingOrder = orderOptional.get();
        existingOrder.setStatus("Completed");
        return modelMapper.map(orderRepository.save(existingOrder),OrderDto.class);
    }

    @Override
    public OrderDto updateChangeTimeOfOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id);
        }
        Order existingOrder = orderOptional.get();
        if(existingOrder.getChangeTimes()==0){
            return modelMapper.map(existingOrder, OrderDto.class);
        }
        existingOrder.setChangeTimes(existingOrder.getChangeTimes()-1);
        return modelMapper.map(orderRepository.save(existingOrder),OrderDto.class);
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
