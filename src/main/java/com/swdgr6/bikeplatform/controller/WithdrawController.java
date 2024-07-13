package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.ResponseDTO;
import com.swdgr6.bikeplatform.model.payload.dto.TransactionDto;
import com.swdgr6.bikeplatform.model.payload.dto.WithdrawBikePointDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.ResponseHandler;
import com.swdgr6.bikeplatform.model.payload.responeModel.TransactionsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.WithdrawBPResponse;
import com.swdgr6.bikeplatform.service.WithdrawBPService;
import com.swdgr6.bikeplatform.service.impl.EmailService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/withdraws")
public class WithdrawController {
    @Autowired
    private WithdrawBPService withdrawBPService;

    @GetMapping
    public WithdrawBPResponse getAllWithdraws(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                 @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                 @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return withdrawBPService.getAllWithdrawBP(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getWithdrawById(@PathVariable("id") Long id){
        WithdrawBikePointDto withdrawBikePointDto = withdrawBPService.getWithdrawBPById(id);
        return new ResponseEntity<>(withdrawBikePointDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping
    public ResponseEntity<?> createWithdraw(@RequestBody WithdrawBikePointDto withdrawBikePointDto){
        WithdrawBikePointDto withdrawBikePointDto1 = withdrawBPService.createWithdrawBikePoint(withdrawBikePointDto);
        return new ResponseEntity<>(withdrawBikePointDto1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("{id}/success")
    public ResponseEntity<?> updateStatusSuccess(@PathVariable("id") Long id){
        WithdrawBikePointDto withdrawBikePointDto1 = withdrawBPService.updateWithdrawBPStatus(id, "Success");
        return new ResponseEntity<>(withdrawBikePointDto1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("{id}/reject")
    public ResponseEntity<?> updateStatusReject(@PathVariable("id") Long id){
        WithdrawBikePointDto withdrawBikePointDto1 = withdrawBPService.updateWithdrawBPStatus(id, "Rejected");
        return new ResponseEntity<>(withdrawBikePointDto1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWithdraw(@PathVariable Long id) {
        String msg = withdrawBPService.deleteWithdrawBP(id);
         return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/bike-points/{bid}")
    public WithdrawBPResponse getAllWithdrawsOfBikePoint(@PathVariable("bid")Long bid ,@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return withdrawBPService.getAllWithdrawOfBikePoint(bid,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/bike-points/{bid}/success")
    public WithdrawBPResponse getAllWithdrawOfBikePointByStatusSuccess(@PathVariable("bid")Long bid,@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                                            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return withdrawBPService.getWithdrawOfBikePointByStatus(bid,"Success",pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/bike-points/{bid}/rejected")
    public WithdrawBPResponse getAllWithdrawsOfBikePointByStatusReject(@PathVariable("bid")Long bid,@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                                           @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return withdrawBPService.getWithdrawOfBikePointByStatus(bid,"Rejected",pageNo, pageSize, sortBy, sortDir);
    }

}
