
package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.*;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;
import com.swdgr6.bikeplatform.model.payload.dto.OrderUsingDto;
import com.swdgr6.bikeplatform.model.payload.dto.TransactionDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.OrderUsingUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrderUsingsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrdersResponse;
import com.swdgr6.bikeplatform.repository.*;
import com.swdgr6.bikeplatform.service.OrderService;
import com.swdgr6.bikeplatform.service.OrderUsingService;
import com.swdgr6.bikeplatform.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class OrderUsingServiceImpl implements OrderUsingService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderUsingRepository orderUsingRepository;

    @Autowired
    private BikePointRepository bikePointRepository;

    @Autowired
    private OrderService orderService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public OrderUsingDto createOrderUsing(OrderUsingDto orderUsingDto) {
        OrderUsing orderUsing = modelMapper.map(orderUsingDto, OrderUsing.class);

        Order order = orderRepository.findById(orderUsingDto.getOrderId())
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Order not found with id: " + orderUsingDto.getOrderId()));
        BikePoint bikePoint = bikePointRepository.findById(orderUsingDto.getBikePointId())
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Bike Point not found with id: " + orderUsingDto.getBikePointId()));
        if (!orderUsing.getDateUsing().isAfter(LocalDate.now())) {
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Date Using should be greater than now");
        }
        if(order.getChangeTimes()==0 || order.getStatus().equalsIgnoreCase("Completed")){
            throw new BikeApiException(HttpStatus.BAD_REQUEST,"The order is not valid.");
        }

        orderUsing.setOrder(order);
        orderUsing.setBikePoint(bikePoint);
        orderUsing.setStatus("Pending");
        orderUsing.setConfirm(false);
        orderUsing.setDelete(false);
        return modelMapper.map(orderUsingRepository.save(orderUsing), OrderUsingDto.class);
    }

    @Override
    public OrderUsingDto updateOrderUsingForUserRating(Long id, OrderUsingUpdatedRequest orderUsingUpdatedRequest) {
        Optional<OrderUsing> orderUsingOptional = orderUsingRepository.findById(id);
        if(orderUsingOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order Using not found");
        }
        OrderUsing orderUsing = orderUsingOptional.get();
        orderUsing.setRating(orderUsingUpdatedRequest.getRating() != 0 ? orderUsingUpdatedRequest.getRating() : orderUsing.getRating() );
        orderUsing.setFeedback(orderUsingUpdatedRequest.getFeedback() != null ? orderUsingUpdatedRequest.getFeedback() : orderUsing.getFeedback() );
        OrderUsing updatedOrderUsing = orderUsingRepository.save(orderUsing);
        return modelMapper.map(updatedOrderUsing, OrderUsingDto.class);
    }

    @Override
    public OrderUsingDto updateOrderUsingFromBikePoint(Long id, double price) {
        Optional<OrderUsing> orderUsingOptional = orderUsingRepository.findById(id);
        if(orderUsingOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order Using not found");
        }
        OrderUsing orderUsing = orderUsingOptional.get();
        ZonedDateTime zonedDateTime = ZonedDateTime.now( ZoneId.of("UTC+07:00"));
        orderUsing.setDateUpdateUsing(zonedDateTime.toLocalDateTime());
        if(!orderUsing.getStatus().equalsIgnoreCase("Pending")){
            throw new BikeApiException(HttpStatus.BAD_REQUEST,"OrderUsing already completed.");
        }
        OrderDto order = orderService.updateChangeTimeOfOrder(orderUsing.getOrder().getId());
        if (order.getChangeTimes()==0){
          orderService.updateStatusOfOrder(orderUsing.getOrder().getId());
        }
        orderUsing.setPrice(price);
        orderUsing.setStatus("Completed");

        return modelMapper.map(orderUsingRepository.save(orderUsing), OrderUsingDto.class);
    }

    @Override
    public OrderUsingDto updateOrderUsingFromBPReject(Long id) {
        Optional<OrderUsing> orderUsingOptional = orderUsingRepository.findById(id);
        if(orderUsingOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order Using not found");
        }
        OrderUsing orderUsing = orderUsingOptional.get();
        ZonedDateTime zonedDateTime = ZonedDateTime.now( ZoneId.of("UTC+07:00"));
        orderUsing.setDateUpdateUsing(zonedDateTime.toLocalDateTime());
        if(!orderUsing.getStatus().equalsIgnoreCase("Pending")){
            throw new BikeApiException(HttpStatus.BAD_REQUEST,"OrderUsing already completed.");
        }
        orderUsing.setStatus("Rejected");
        return modelMapper.map(orderUsingRepository.save(orderUsing), OrderUsingDto.class);
    }

    @Override
    public OrderUsingDto updateOrderUsingFromUser(Long id) {
        Optional<OrderUsing> orderUsingOptional = orderUsingRepository.findById(id);
        if(orderUsingOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order Using not found");
        }
        OrderUsing orderUsing = orderUsingOptional.get();
        orderUsing.setConfirm(true);
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setPayAmount(orderUsing.getPrice());
        transactionDto.setOrderUsingId(orderUsing.getId());
        transactionDto.setWalletId(orderUsing.getBikePoint().getWallet().getId());
        transactionService.createTransaction(transactionDto);
        return modelMapper.map(orderUsingRepository.save(orderUsing), OrderUsingDto.class);

    }

    @Override
    public String deleteOrderUsing(Long id) {
        Optional<OrderUsing> orderUsing = orderUsingRepository.findById(id);
        if(orderUsing.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order Using not found");
        }
        orderUsing.get().setDelete(true);
        orderUsingRepository.save(orderUsing.get());
        return "Order Using was deleted.";
    }

    @Override
    public OrderUsingDto getOrderUsingById(Long id) {
        Optional<OrderUsing> orderUsing = orderUsingRepository.findById(id);
        if(orderUsing.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Order Using not found");
        }
        return modelMapper.map(orderUsing.get(), OrderUsingDto.class);
    }

    @Override
    public OrderUsingsResponse getAllOrderUsing(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderUsing> orderUsings = orderUsingRepository.findAllNotDeleted(pageable);
        // get content for page object
        List<OrderUsing> listOfOrderUsings = orderUsings.getContent();

        List<OrderUsingDto> content = listOfOrderUsings.stream().map(op -> modelMapper.map(op, OrderUsingDto.class)).collect(Collectors.toList());

        OrderUsingsResponse templatesResponse = new OrderUsingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orderUsings.getNumber());
        templatesResponse.setPageSize(orderUsings.getSize());
        templatesResponse.setTotalElements(orderUsings.getTotalElements());
        templatesResponse.setTotalPages(orderUsings.getTotalPages());
        templatesResponse.setLast(orderUsings.isLast());

        return templatesResponse;
    }

    @Override
    public OrderUsingsResponse getOrderUsingsOfBikePoint(Long bikePointId, int pageNo, int pageSize, String sortBy, String sortDir) {
        BikePoint bikePoint = bikePointRepository.findById(bikePointId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Bike Point not found with id: " + bikePointId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderUsing> orderUsings = orderUsingRepository.findOrderUsingsByBikePointAndIsDeleteFalseAndIsConfirmFalse(bikePoint, pageable);
        // get content for page object
        List<OrderUsing> listOfOrderUsings = orderUsings.getContent();

        List<OrderUsingDto> content = listOfOrderUsings.stream().map(op -> modelMapper.map(op, OrderUsingDto.class)).collect(Collectors.toList());

        OrderUsingsResponse templatesResponse = new OrderUsingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orderUsings.getNumber());
        templatesResponse.setPageSize(orderUsings.getSize());
        templatesResponse.setTotalElements(orderUsings.getTotalElements());
        templatesResponse.setTotalPages(orderUsings.getTotalPages());
        templatesResponse.setLast(orderUsings.isLast());

        return templatesResponse;
    }

    @Override
    public OrderUsingsResponse getOrderUsingsOfBikePointIsConfirm(Long bikePointId, int pageNo, int pageSize, String sortBy, String sortDir){
        BikePoint bikePoint = bikePointRepository.findById(bikePointId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Bike Point not found with id: " + bikePointId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderUsing> orderUsings = orderUsingRepository.findOrderUsingsByBikePointAndIsDeleteFalseAndIsConfirmTrue(bikePoint, pageable);
        // get content for page object
        List<OrderUsing> listOfOrderUsings = orderUsings.getContent();

        List<OrderUsingDto> content = listOfOrderUsings.stream().map(op -> modelMapper.map(op, OrderUsingDto.class)).collect(Collectors.toList());

        OrderUsingsResponse templatesResponse = new OrderUsingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orderUsings.getNumber());
        templatesResponse.setPageSize(orderUsings.getSize());
        templatesResponse.setTotalElements(orderUsings.getTotalElements());
        templatesResponse.setTotalPages(orderUsings.getTotalPages());
        templatesResponse.setLast(orderUsings.isLast());

        return templatesResponse;
    }

    @Override
    public OrderUsingsResponse getOrderUsingsOfOrder(Long orderId, int pageNo, int pageSize, String sortBy, String sortDir) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Order not found with id: " + orderId));


        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderUsing> orderUsings = orderUsingRepository.findOrderUsingsByOrderAndIsDeleteFalse(order, pageable);
        // get content for page object
        List<OrderUsing> listOfOrderUsings = orderUsings.getContent();

        List<OrderUsingDto> content = listOfOrderUsings.stream().map(op -> modelMapper.map(op, OrderUsingDto.class)).collect(Collectors.toList());

        OrderUsingsResponse templatesResponse = new OrderUsingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orderUsings.getNumber());
        templatesResponse.setPageSize(orderUsings.getSize());
        templatesResponse.setTotalElements(orderUsings.getTotalElements());
        templatesResponse.setTotalPages(orderUsings.getTotalPages());
        templatesResponse.setLast(orderUsings.isLast());

        return templatesResponse;
    }

    @Override
    public OrderUsingsResponse getOrderUsingsByStatus(String status,int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderUsing> orderUsings = orderUsingRepository.findOrderUsingsByStatusAndIsDeleteFalse(status, pageable);
        // get content for page object
        List<OrderUsing> listOfOrderUsings = orderUsings.getContent();

        List<OrderUsingDto> content = listOfOrderUsings.stream().map(op -> modelMapper.map(op, OrderUsingDto.class)).collect(Collectors.toList());

        OrderUsingsResponse templatesResponse = new OrderUsingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orderUsings.getNumber());
        templatesResponse.setPageSize(orderUsings.getSize());
        templatesResponse.setTotalElements(orderUsings.getTotalElements());
        templatesResponse.setTotalPages(orderUsings.getTotalPages());
        templatesResponse.setLast(orderUsings.isLast());

        return templatesResponse;
    }

    @Override
    public OrderUsingsResponse getOrderUsingsByStatusWithBikePoint(Long bikePointId, String status, int pageNo, int pageSize, String sortBy, String sortDir) {
        BikePoint bikePoint = bikePointRepository.findById(bikePointId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Bike Point not found with id: " + bikePointId));


        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderUsing> orderUsings = orderUsingRepository.findOrderUsingsByBikePointAndIsDeleteFalseAndStatus(bikePoint, status, pageable);
        // get content for page object
        List<OrderUsing> listOfOrderUsings = orderUsings.getContent();

        List<OrderUsingDto> content = listOfOrderUsings.stream().map(op -> modelMapper.map(op, OrderUsingDto.class)).collect(Collectors.toList());

        OrderUsingsResponse templatesResponse = new OrderUsingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orderUsings.getNumber());
        templatesResponse.setPageSize(orderUsings.getSize());
        templatesResponse.setTotalElements(orderUsings.getTotalElements());
        templatesResponse.setTotalPages(orderUsings.getTotalPages());
        templatesResponse.setLast(orderUsings.isLast());

        return templatesResponse;
    }

    @Override
    public OrderUsingsResponse getOrderUsingsByStatusWithUser(Long uid, String status, int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"User not found with id: " + uid));


        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderUsing> orderUsings = orderUsingRepository.findOrderUsingsByOrder_UserAndIsDeleteFalseAndStatus(user, status, pageable);
        // get content for page object
        List<OrderUsing> listOfOrderUsings = orderUsings.getContent();

        List<OrderUsingDto> content = listOfOrderUsings.stream().map(op -> modelMapper.map(op, OrderUsingDto.class)).collect(Collectors.toList());

        OrderUsingsResponse templatesResponse = new OrderUsingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orderUsings.getNumber());
        templatesResponse.setPageSize(orderUsings.getSize());
        templatesResponse.setTotalElements(orderUsings.getTotalElements());
        templatesResponse.setTotalPages(orderUsings.getTotalPages());
        templatesResponse.setLast(orderUsings.isLast());

        return templatesResponse;
    }


    @Override
    public OrderUsingsResponse getOrderUsingsByUser(Long uid, int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"User not found with id: " + uid));


        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OrderUsing> orderUsings = orderUsingRepository.findOrderUsingsByOrder_UserAndIsDeleteFalse(user, pageable);
        // get content for page object
        List<OrderUsing> listOfOrderUsings = orderUsings.getContent();

        List<OrderUsingDto> content = listOfOrderUsings.stream().map(op -> modelMapper.map(op, OrderUsingDto.class)).collect(Collectors.toList());

        OrderUsingsResponse templatesResponse = new OrderUsingsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(orderUsings.getNumber());
        templatesResponse.setPageSize(orderUsings.getSize());
        templatesResponse.setTotalElements(orderUsings.getTotalElements());
        templatesResponse.setTotalPages(orderUsings.getTotalPages());
        templatesResponse.setLast(orderUsings.isLast());

        return templatesResponse;
    }
}

