package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.OrderDTO;
import com.swdgr6.bikeplatform.model.payload.dto.ResponseDTO;
import com.swdgr6.bikeplatform.model.payload.responeModel.ResponseHandler;
import com.swdgr6.bikeplatform.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllOrders() {
        try {
            return ResponseHandler.DataResponse(orderService.getAllOrders(), "");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex,"api/v1/auth/order/all");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createOrder(@RequestBody OrderDTO orderDto) {
        try {
            return ResponseHandler.DataResponse(orderService.createOrder(orderDto), "Created successfully");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex,"api/v1/auth/order/create");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateOrder(@RequestBody OrderDTO orderDto) {
        try {
            return ResponseHandler.DataResponse(orderService.updateOrder(orderDto), "Updated successfully");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex,"api/v1/auth/order/update");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteOrder(@RequestParam Long id) {
        try {
            return ResponseHandler.DataResponse(orderService.deleteOrder(id), "Deleted successfully");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex,"api/v1/auth/order/delete");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO> searchOrders(@RequestParam String searchTerm) {
        try {
            List<OrderDTO> searchResults = orderService.searchOrders(searchTerm);
            if (searchResults.isEmpty()) {
                return ResponseHandler.DataResponse(searchResults, "No orders found. They may have been deleted.");
            } else {
                return ResponseHandler.DataResponse(searchResults, "Search completed successfully");
            }
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex,"api/v1/auth/order/search");
        }
    }
}
