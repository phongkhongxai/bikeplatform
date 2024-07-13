package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.*;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.OrderUsingDto;
import com.swdgr6.bikeplatform.model.payload.dto.TransactionDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrderUsingsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.TransactionsResponse;
import com.swdgr6.bikeplatform.repository.*;
import com.swdgr6.bikeplatform.service.OrderService;
import com.swdgr6.bikeplatform.service.TransactionService;
import com.swdgr6.bikeplatform.service.WalletService;
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
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BikePointRepository bikePointRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private OrderUsingRepository orderUsingRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        transaction.setDelete(false);
        OrderUsing orderUsing = orderUsingRepository.findById(transactionDto.getOrderUsingId())
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Order Using not found with ID: " + transactionDto.getOrderUsingId()));
        Wallet wallet = walletRepository.findById(transactionDto.getWalletId())
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Wallet not found with ID: " + transactionDto.getWalletId()));
        transaction.setWallet(wallet);
        transaction.setOrderUsing(orderUsing);
        transaction.setDate(LocalDateTime.now());
        transaction.setPayAmount(orderUsing.getPrice());
        transaction.setStatus("Pending");
        return modelMapper.map(transactionRepository.save(transaction), TransactionDto.class);
    }

    @Override
    public TransactionDto updateTransactionSuccess(Long id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if(transactionOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Transaction not found");
        }
        Transaction transaction = transactionOptional.get();
        transaction.setStatus("Success");
        walletService.updateWalletPlusCash(transaction.getWallet().getId(), transaction.getPayAmount());
        emailService.sendSuccessTransactionEmail(transaction.getWallet().getBikePoint().getUser().getEmail(),transaction.getWallet().getBikePoint().getUser().getFullName(), transaction.getWallet().getBikePoint().getName(), transaction.getPayAmount(), String.valueOf(transaction.getDate()));
        transaction.setDate(LocalDateTime.now());
        return modelMapper.map(transactionRepository.save(transaction), TransactionDto.class);

    }

    @Override
    public TransactionDto updateTransactionReject(Long id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if(transactionOptional.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Transaction not found");
        }
        Transaction transaction = transactionOptional.get();
        transaction.setStatus("Rejected");
        transaction.setDate(LocalDateTime.now());
        emailService.sendFailureTransactionEmail(transaction.getWallet().getBikePoint().getUser().getEmail(),transaction.getWallet().getBikePoint().getUser().getFullName(), transaction.getWallet().getBikePoint().getName(), transaction.getPayAmount(),"Giá của bạn không hợp lí.", String.valueOf(transaction.getDate()));
        return modelMapper.map(transactionRepository.save(transaction), TransactionDto.class);
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if(transaction.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Transaction not found");
        }
        return modelMapper.map(transaction.get(), TransactionDto.class);
    }

    @Override
    public TransactionsResponse getAllTransaction(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Transaction> transactions = transactionRepository.findAllNotDeleted(pageable);
        // get content for page object
        List<Transaction> transactionList = transactions.getContent();

        List<TransactionDto> content = transactionList.stream().map(op -> modelMapper.map(op, TransactionDto.class)).collect(Collectors.toList());

        TransactionsResponse templatesResponse = new TransactionsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(transactions.getNumber());
        templatesResponse.setPageSize(transactions.getSize());
        templatesResponse.setTotalElements(transactions.getTotalElements());
        templatesResponse.setTotalPages(transactions.getTotalPages());
        templatesResponse.setLast(transactions.isLast());

        return templatesResponse;
    }

    @Override
    public TransactionsResponse getAllTransactionofBikePoint(Long id, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        BikePoint bikePoint = bikePointRepository.findById(id)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "BikePoint not found with ID: " + id));
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Transaction> transactions = transactionRepository.findTransactionsByOrderUsingBikePointAndIsDeleteFalse(bikePoint, pageable);
        // get content for page object
        List<Transaction> transactionList = transactions.getContent();

        List<TransactionDto> content = transactionList.stream().map(op -> modelMapper.map(op, TransactionDto.class)).collect(Collectors.toList());

        TransactionsResponse templatesResponse = new TransactionsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(transactions.getNumber());
        templatesResponse.setPageSize(transactions.getSize());
        templatesResponse.setTotalElements(transactions.getTotalElements());
        templatesResponse.setTotalPages(transactions.getTotalPages());
        templatesResponse.setLast(transactions.isLast());

        return templatesResponse;
    }

    @Override
    public TransactionsResponse getAllTransactionOfBikePointByStatus(Long id, String status, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        BikePoint bikePoint = bikePointRepository.findById(id)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "BikePoint not found with ID: " + id));
        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Transaction> transactions = transactionRepository.findTransactionsByOrderUsingBikePointAndIsDeleteFalseAndStatus(bikePoint, status, pageable);
        // get content for page object
        List<Transaction> transactionList = transactions.getContent();

        List<TransactionDto> content = transactionList.stream().map(op -> modelMapper.map(op, TransactionDto.class)).collect(Collectors.toList());

        TransactionsResponse templatesResponse = new TransactionsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(transactions.getNumber());
        templatesResponse.setPageSize(transactions.getSize());
        templatesResponse.setTotalElements(transactions.getTotalElements());
        templatesResponse.setTotalPages(transactions.getTotalPages());
        templatesResponse.setLast(transactions.isLast());
        return templatesResponse;
    }
}
