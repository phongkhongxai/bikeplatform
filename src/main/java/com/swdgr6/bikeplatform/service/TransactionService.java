package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.TransactionDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.TransactionsResponse;

public interface TransactionService {
    TransactionDto createTransaction(TransactionDto transactionDto);
    TransactionDto updateTransactionSuccess(Long id);
    TransactionDto updateTransactionReject(Long id);
    TransactionDto getTransactionById(Long id);

    TransactionsResponse getAllTransaction(int pageNo, int pageSize, String sortBy, String sortDir);
    TransactionsResponse getAllTransactionofBikePoint(Long id,int pageNo, int pageSize, String sortBy, String sortDir);

    TransactionsResponse getAllTransactionOfBikePointByStatus(Long id, String status,int pageNo, int pageSize, String sortBy, String sortDir);
}
