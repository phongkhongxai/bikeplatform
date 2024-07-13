package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.ResponseDTO;
import com.swdgr6.bikeplatform.model.payload.dto.WalletDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.ResponseHandler;
import com.swdgr6.bikeplatform.service.WalletService;
import com.swdgr6.bikeplatform.service.impl.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/auth/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable("id") Long id){
        WalletDto walletDto = walletService.getWalletById(id);
        return new ResponseEntity<>(walletDto, HttpStatus.OK);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/plus-cash")
    public ResponseEntity<?> updateWalletPlusCash(@PathVariable Long id, @RequestParam(value = "amount") double amount) {
        walletService.updateWalletPlusCash(id, amount);
        return new ResponseEntity<>("Successful.", HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWalletInfo(@PathVariable Long id, @RequestBody WalletDto walletDto) {
         WalletDto walletDto1 = walletService.updateWalletInfo(id, walletDto);
         return new ResponseEntity<>(walletDto1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteWallet(@PathVariable Long id) {
        try {
            return ResponseHandler.DataResponse(walletService.deleteWallet(id), "Wallet deleted successfully");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex, RequestMethod.DELETE, "api/v1/auth/wallet/delete-wallet/" + id);
        }
    }


}
