package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.ResponseDTO;
import com.swdgr6.bikeplatform.model.payload.responeModel.ResponseHandler;
import com.swdgr6.bikeplatform.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
