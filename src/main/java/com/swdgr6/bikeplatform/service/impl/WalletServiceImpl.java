package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.Wallet;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.WalletDto;
import com.swdgr6.bikeplatform.repository.BikePointRepository;
import com.swdgr6.bikeplatform.repository.WalletRepository;
import com.swdgr6.bikeplatform.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private BikePointRepository bikePointRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void updateWalletPlusCash(Long id, double payAmount) {
        Optional<Wallet> existingWalletOptional = walletRepository.findById(id);
        if (existingWalletOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Wallet not found");
        }
        Wallet wallet = existingWalletOptional.get();
        wallet.setBalance(wallet.getBalance()+payAmount);
        walletRepository.save(wallet);
    }

    @Override
    public String deleteWallet(Long id) {
        Optional<Wallet> walletOptional = walletRepository.findById(id);
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            wallet.setDelete(true);
            walletRepository.save(wallet);
            return "Wallet with ID: " + id + " marked as deleted successfully";
        }
        return "Wallet with ID: " + id + " not found";
    }

    @Override
    public WalletDto updateWalletInfo(Long id, WalletDto walletDto) {
        Optional<Wallet> existingWalletOptional = walletRepository.findById(id);
        if (existingWalletOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Wallet not found");
        }
        Wallet wallet = existingWalletOptional.get();
        wallet.setBankName(walletDto.getBankName());
        wallet.setAccountNumber(walletDto.getAccountNumber());
        return modelMapper.map(walletRepository.save(wallet), WalletDto.class);
    }

    @Override
    public WalletDto getWalletById(Long id) {
        Optional<Wallet> existingWalletOptional = walletRepository.findById(id);
        if (existingWalletOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Wallet not found");
        }
        return modelMapper.map(existingWalletOptional.get(), WalletDto.class);
    }

}
