package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.WalletDto;

public interface WalletService {
    void updateWalletPlusCash(Long id, double payAmount);
    String deleteWallet(Long id);
    WalletDto updateWalletInfo(Long id, WalletDto walletDto);
    WalletDto getWalletById(Long id);

}
