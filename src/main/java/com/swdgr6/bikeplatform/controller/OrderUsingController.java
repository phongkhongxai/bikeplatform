package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.entity.OrderUsing;
import com.swdgr6.bikeplatform.model.payload.dto.OrderUsingDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.OrderUsingUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrderUsingsResponse;
import com.swdgr6.bikeplatform.service.OrderUsingService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-usings")
public class OrderUsingController {
    @Autowired
    private OrderUsingService orderUsingService;


    @GetMapping
    public OrderUsingsResponse getAllOrderUsing(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getAllOrderUsing(pageNo, pageSize, sortBy, sortDir);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderUsingById(@PathVariable("id") Long id){
        OrderUsingDto orderUsingDto = orderUsingService.getOrderUsingById(id);
        return new ResponseEntity<>(orderUsingDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createOrderUsing(@Valid @RequestBody OrderUsingDto orderUsingDto){
        OrderUsingDto orderUsingDto1 = orderUsingService.createOrderUsing(orderUsingDto);
        return new ResponseEntity<>(orderUsingDto1, HttpStatus.OK);

    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderUsing(@PathVariable("id")Long id){
        String msg = orderUsingService.deleteOrderUsing(id);
        return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("/bike-points/{id}")
    public ResponseEntity<?> updateStatusFromBikePoint(@PathVariable("id")Long id, @RequestParam(value = "price") double price){
        OrderUsingDto orderUsingDto = orderUsingService.updateOrderUsingFromBikePoint(id, price);
        return new ResponseEntity<>(orderUsingDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("/bike-points/{id}/rejected")
    public ResponseEntity<?> updateOrderUsingFromBPReject(@PathVariable("id")Long id){
        OrderUsingDto orderUsingDto = orderUsingService.updateOrderUsingFromBPReject(id);
        return new ResponseEntity<>(orderUsingDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/user/{id}/confirmed")
    public ResponseEntity<?> updateOrderUsingFromUser(@PathVariable("id")Long id){
        OrderUsingDto orderUsingDto = orderUsingService.updateOrderUsingFromUser(id);
        return new ResponseEntity<>(orderUsingDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/{id}/user/feedbacks")
    public ResponseEntity<?> updateOrderUsingForUserRating(@PathVariable("id")Long id, @RequestBody OrderUsingUpdatedRequest orderUsingUpdatedRequest){
        OrderUsingDto orderUsingDto = orderUsingService.updateOrderUsingForUserRating(id, orderUsingUpdatedRequest);
        return new ResponseEntity<>(orderUsingDto, HttpStatus.OK);
    }

    @GetMapping("/bike-points/{bid}/completed")
    public OrderUsingsResponse getOrderUsingsCompletedforShop(@PathVariable("bid")Long bid,@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getOrderUsingsOfBikePointIsConfirm(bid,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/bike-points/{bid}/uncompleted")
    public OrderUsingsResponse getOrderUsingsUncompletedforShop(@PathVariable("bid")Long bid, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getOrderUsingsOfBikePoint(bid,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/bike-points/{bid}/pending")
    public OrderUsingsResponse getOrderUsingsPendingforShop(@PathVariable("bid")Long bid, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getOrderUsingsByStatusWithBikePoint(bid, "Pending",pageNo, pageSize, sortBy, sortDir);

    }

    @GetMapping("/bike-points/{bid}/rejected")
    public OrderUsingsResponse getOrderUsingsRejectedforShop(@PathVariable("bid")Long bid, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getOrderUsingsByStatusWithBikePoint(bid, "Rejected",pageNo, pageSize, sortBy, sortDir);
    }


    @GetMapping("/users/orders/{id}")
    public OrderUsingsResponse getOrderUsingsByOrder(@PathVariable("id")Long id, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                 @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                 @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getOrderUsingsOfOrder(id ,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/users/{uid}/uncompleted")
    public OrderUsingsResponse getOrderUsingsRejectedByUser(@PathVariable("uid")Long id, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getOrderUsingsByStatusWithUser(id ,"Rejected",pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/users/{uid}/pending")
    public OrderUsingsResponse getOrderUsingsPendingByUser(@PathVariable("uid")Long id, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getOrderUsingsByStatusWithUser(id,"Pending" ,pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/users/{uid}/completed")
    public OrderUsingsResponse getOrderUsingscCompletedByUser(@PathVariable("uid")Long id, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return orderUsingService.getOrderUsingsByStatusWithUser(id,"Completed" ,pageNo, pageSize, sortBy, sortDir);
    }
}
