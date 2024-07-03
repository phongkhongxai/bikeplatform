package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.WalletDto;

import java.util.List;

public interface WalletService {
    WalletDto updateWallet(WalletDto walletDto);
    String deleteWallet(Long id);
}
