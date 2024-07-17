package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.BikePoint;
import com.swdgr6.bikeplatform.model.entity.OrderUsing;
import com.swdgr6.bikeplatform.model.entity.Transaction;
import com.swdgr6.bikeplatform.model.entity.WithdrawBikePoint;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.TransactionDto;
import com.swdgr6.bikeplatform.model.payload.dto.WithdrawBikePointDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.TransactionsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.WithdrawBPResponse;
import com.swdgr6.bikeplatform.repository.BikePointRepository;
import com.swdgr6.bikeplatform.repository.WithdrawBPRepository;
import com.swdgr6.bikeplatform.service.WalletService;
import com.swdgr6.bikeplatform.service.WithdrawBPService;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WithdrawBPServiceImpl implements WithdrawBPService {
    @Autowired
    private WithdrawBPRepository withdrawBPRepository;
    @Autowired
    private BikePointRepository bikePointRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private WalletService walletService;

    @Override
    public WithdrawBikePointDto createWithdrawBikePoint(WithdrawBikePointDto withdrawBikePointDto) {
        WithdrawBikePoint withdrawBikePoint = modelMapper.map(withdrawBikePointDto, WithdrawBikePoint.class);
        BikePoint bikePoint = bikePointRepository.findById(withdrawBikePointDto.getBikePointId())
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Bike Point not found with ID: " + withdrawBikePointDto.getBikePointId()));
        withdrawBikePoint.setDelete(false);
        if (bikePoint.getWallet().getBalance() == 0 || withdrawBikePointDto.getAmount() > bikePoint.getWallet().getBalance()) {
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Insufficient balance in the wallet");
        }
        withdrawBikePoint.setBikePoint(bikePoint);
        withdrawBikePoint.setAmount(withdrawBikePointDto.getAmount());
        withdrawBikePoint.setDate(LocalDateTime.now());
        withdrawBikePoint.setStatus("Pending");
        return modelMapper.map(withdrawBPRepository.save(withdrawBikePoint), WithdrawBikePointDto.class);
    }

    @Override
    public WithdrawBikePointDto getWithdrawBPById(Long id) {
        Optional<WithdrawBikePoint> withdrawBikePointOptional = withdrawBPRepository.findById(id);
        if(withdrawBikePointOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Withdraw Bike point not found");
        }
        return modelMapper.map(withdrawBikePointOptional.get(), WithdrawBikePointDto.class);
    }

    @Override
    public WithdrawBikePointDto updateWithdrawBPStatus(Long id, String status) {
        Optional<WithdrawBikePoint> withdrawBikePointOptional = withdrawBPRepository.findById(id);
        if(withdrawBikePointOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Withdraw Bike point not found");
        }
        WithdrawBikePoint withdrawBikePoint = withdrawBikePointOptional.get();
        withdrawBikePoint.setStatus(status);
        if(status.equalsIgnoreCase("Success")){
            walletService.updateWalletPlusCash(withdrawBikePoint.getBikePoint().getWallet().getId(),-withdrawBikePoint.getAmount());
            emailService.sendSuccessWithdrawEmail(withdrawBikePoint.getBikePoint().getUser().getEmail(),withdrawBikePoint.getBikePoint().getUser().getFullName(),withdrawBikePoint.getAmount(), String.valueOf(withdrawBikePoint.getDate()));
        }
        if(status.equalsIgnoreCase("Rejected")){
            emailService.sendFailureWithdrawEmail(withdrawBikePoint.getBikePoint().getUser().getEmail(),withdrawBikePoint.getBikePoint().getUser().getFullName(),withdrawBikePoint.getAmount(), String.valueOf(withdrawBikePoint.getDate()), "Lỗi không xác định");
        }
        return modelMapper.map(withdrawBPRepository.save(withdrawBikePoint), WithdrawBikePointDto.class);
    }

    @Override
    public String deleteWithdrawBP(Long id) {
        Optional<WithdrawBikePoint> withdrawBikePointOptional = withdrawBPRepository.findById(id);
        if(withdrawBikePointOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Withdraw Bike point not found");
        }
        WithdrawBikePoint withdrawBikePoint = withdrawBikePointOptional.get();
        withdrawBikePoint.setDelete(true);
        withdrawBPRepository.save(withdrawBikePoint);
        return "WithdrawBikePoint deleted successful.";
    }

    @Override
    public WithdrawBPResponse getAllWithdrawBP(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<WithdrawBikePoint> withdrawBikePoints = withdrawBPRepository.findAllNotDeleted(pageable);
        // get content for page object
        List<WithdrawBikePoint> withdrawBikePointList = withdrawBikePoints.getContent();

        List<WithdrawBikePointDto> content = withdrawBikePointList.stream().map(op -> modelMapper.map(op, WithdrawBikePointDto.class)).collect(Collectors.toList());

        WithdrawBPResponse templatesResponse = new WithdrawBPResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(withdrawBikePoints.getNumber());
        templatesResponse.setPageSize(withdrawBikePoints.getSize());
        templatesResponse.setTotalElements(withdrawBikePoints.getTotalElements());
        templatesResponse.setTotalPages(withdrawBikePoints.getTotalPages());
        templatesResponse.setLast(withdrawBikePoints.isLast());

        return templatesResponse;
    }

    @Override
    public WithdrawBPResponse getAllWithdrawOfBikePoint(Long bid, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        BikePoint bikePoint = bikePointRepository.findById(bid)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Bike Point not found with ID: " + bid));
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<WithdrawBikePoint> withdrawBikePoints = withdrawBPRepository.findWithdrawBikePointsByBikePointAndIsDeleteFalse(bikePoint,pageable);
        // get content for page object
        List<WithdrawBikePoint> withdrawBikePointList = withdrawBikePoints.getContent();

        List<WithdrawBikePointDto> content = withdrawBikePointList.stream().map(op -> modelMapper.map(op, WithdrawBikePointDto.class)).collect(Collectors.toList());

        WithdrawBPResponse templatesResponse = new WithdrawBPResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(withdrawBikePoints.getNumber());
        templatesResponse.setPageSize(withdrawBikePoints.getSize());
        templatesResponse.setTotalElements(withdrawBikePoints.getTotalElements());
        templatesResponse.setTotalPages(withdrawBikePoints.getTotalPages());
        templatesResponse.setLast(withdrawBikePoints.isLast());

        return templatesResponse;
    }

    @Override
    public WithdrawBPResponse getWithdrawOfBikePointByStatus(Long bid, String status, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        BikePoint bikePoint = bikePointRepository.findById(bid)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Bike Point not found with ID: " + bid));
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<WithdrawBikePoint> withdrawBikePoints = withdrawBPRepository.findWithdrawBikePointsByBikePointAndIsDeleteFalseAndStatus(bikePoint,status,pageable);
        // get content for page object
        List<WithdrawBikePoint> withdrawBikePointList = withdrawBikePoints.getContent();

        List<WithdrawBikePointDto> content = withdrawBikePointList.stream().map(op -> modelMapper.map(op, WithdrawBikePointDto.class)).collect(Collectors.toList());

        WithdrawBPResponse templatesResponse = new WithdrawBPResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(withdrawBikePoints.getNumber());
        templatesResponse.setPageSize(withdrawBikePoints.getSize());
        templatesResponse.setTotalElements(withdrawBikePoints.getTotalElements());
        templatesResponse.setTotalPages(withdrawBikePoints.getTotalPages());
        templatesResponse.setLast(withdrawBikePoints.isLast());

        return templatesResponse;
    }
}
