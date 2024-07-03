package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.ResponseDTO;
import com.swdgr6.bikeplatform.model.payload.dto.WalletDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.ResponseHandler;
import com.swdgr6.bikeplatform.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PutMapping("/update-wallet")
    public ResponseEntity<ResponseDTO> updateWallet(@RequestBody WalletDto walletDto) {
        try {
            return ResponseHandler.DataResponse(walletService.updateWallet(walletDto), "Wallet updated successfully");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex, RequestMethod.PUT, "api/v1/auth/wallet/update-wallet");
        }
    }

    @DeleteMapping("/delete-wallet/{id}")
    public ResponseEntity<ResponseDTO> deleteWallet(@PathVariable Long id) {
        try {
            return ResponseHandler.DataResponse(walletService.deleteWallet(id), "Wallet deleted successfully");
        } catch (Exception ex) {
            return ResponseHandler.ErrorResponse(HttpStatus.BAD_REQUEST, ex, RequestMethod.DELETE, "api/v1/auth/wallet/delete-wallet/" + id);
        }
    }
}
