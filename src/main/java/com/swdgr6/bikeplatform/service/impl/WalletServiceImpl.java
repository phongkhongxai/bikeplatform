package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.BikePoint;
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
    public WalletDto updateWallet(WalletDto walletDto) {
        Optional<Wallet> existingWalletOptional = walletRepository.findById(walletDto.getId());
        if (existingWalletOptional.isPresent()) {
            Wallet existingWallet = existingWalletOptional.get();
            existingWallet.setBalance(walletDto.getBalance());
            existingWallet.setAccountNumber(walletDto.getAccountNumber());
            existingWallet.setBankName(walletDto.getBankName());

            Optional<BikePoint> bikePointOptional = bikePointRepository.findById(walletDto.getBikePointId());
            if (bikePointOptional.isEmpty()) {
                throw new BikeApiException(HttpStatus.NOT_FOUND, "BikePoint not found with ID: " + walletDto.getBikePointId());
            }
            existingWallet.setBikePoint(bikePointOptional.get());

            Wallet updatedWallet = walletRepository.save(existingWallet);
            return modelMapper.map(updatedWallet, WalletDto.class);
        } else {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Wallet not found with ID: " + walletDto.getId());
        }
    }

    @Override
    public String deleteWallet(Long id) {
        Optional<Wallet> walletOptional = walletRepository.findById(id);
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            wallet.setDeleted(true);
            walletRepository.save(wallet);
            return "Wallet with ID: " + id + " marked as deleted successfully";
        }
        return "Wallet with ID: " + id + " not found";
    }

}
