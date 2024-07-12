package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;
import com.swdgr6.bikeplatform.model.payload.dto.ResponseDTO;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrdersResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.ResponseHandler;
import com.swdgr6.bikeplatform.service.OrderService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @GetMapping
    public ResponseEntity<ResponseDTO> getAllOrders(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        try {
            return ResponseHandler.DataResponse(orderService.getAllOrders(pageNo, pageSize, sortBy, sortDir), "");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex, RequestMethod.GET,"api/v1/auth/orders");
        }
    }

    @GetMapping("/by-user/{uid}")
    public OrdersResponse getAllOrdersOfUser(@PathVariable("uid") Long uid, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                             @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
         return orderService.getAllOrdersByUser(uid, pageNo, pageSize, sortBy, sortDir);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<ResponseDTO> createOrder(@Valid @RequestBody OrderDto orderDto) {
        try {
            return ResponseHandler.DataResponse(orderService.createOrder(orderDto), "Created successfully");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex, RequestMethod.POST , "api/v1/orders");
        }
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/{id}/vehicles/{vehicle_id}")
    public ResponseEntity<?> updateVehicleOfOrder(@PathVariable("id") Long id, @PathVariable("vehicle_id") Long vehicleId) {
         OrderDto order = orderService.updateVehicleForOrder(id, vehicleId);
         return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteOrder(@PathVariable("id") Long id) {
        try {
            return ResponseHandler.DataResponse(orderService.deleteOrder(id), "Deleted successfully");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex, RequestMethod.DELETE,"api/v1/orders");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO> searchOrders(@RequestParam String searchTerm) {
        try {
            List<OrderDto> searchResults = orderService.searchOrders(searchTerm);
            if (searchResults.isEmpty()) {
                return ResponseHandler.DataResponse(searchResults, "No orders found. They may have been deleted.");
            } else {
                return ResponseHandler.DataResponse(searchResults, "Search completed successfully");
            }
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex, RequestMethod.GET,"api/v1/orders/search");
        }
    }
}
