package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.entity.Transaction;
import com.swdgr6.bikeplatform.model.payload.dto.OrderUsingDto;
import com.swdgr6.bikeplatform.model.payload.dto.TransactionDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrderUsingsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.TransactionsResponse;
import com.swdgr6.bikeplatform.service.TransactionService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public TransactionsResponse getAllTransactions(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                 @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                 @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return transactionService.getAllTransaction(pageNo, pageSize, sortBy, sortDir);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable("id") Long id){
        TransactionDto transactionDto = transactionService.getTransactionById(id);
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestParam(value = "amount") double payAmount, @RequestParam(value = "orderUsingId") Long orderUsingId, @RequestParam(value = "walletId") Long walletId ){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setWalletId(walletId);
        transactionDto.setOrderUsingId(orderUsingId);
        transactionDto.setPayAmount(payAmount);
        TransactionDto transactionDto1 = transactionService.createTransaction(transactionDto);
        return new ResponseEntity<>(transactionDto1, HttpStatus.OK);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/success")
    public ResponseEntity<?> updateStatusSuccess(@PathVariable("id")Long id ){
        TransactionDto transactionDto = transactionService.updateTransactionSuccess(id );
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/rejected")
    public ResponseEntity<?> updateStatusReject(@PathVariable("id")Long id ){
        TransactionDto transactionDto = transactionService.updateTransactionReject(id);
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }
    @GetMapping("/bike-points/{bid}")
    public TransactionsResponse getAllTransactionsOfBikePoint(@PathVariable("bid")Long bid ,@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return transactionService.getAllTransactionofBikePoint(bid,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/bike-points/{bid}/success")
    public TransactionsResponse getAllTransactionOfBikePointByStatusSuccess(@PathVariable("bid")Long bid,@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                   @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return transactionService.getAllTransactionOfBikePointByStatus(bid,"Success",pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/bike-points/{bid}/rejected")
    public TransactionsResponse getAllTransactionOfBikePointByStatusReject(@PathVariable("bid")Long bid,@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        return transactionService.getAllTransactionOfBikePointByStatus(bid,"Rejected",pageNo, pageSize, sortBy, sortDir);
    }
}
