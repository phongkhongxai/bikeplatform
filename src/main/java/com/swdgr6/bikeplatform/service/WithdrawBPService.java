package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.WithdrawBikePointDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.WithdrawBPResponse;

public interface WithdrawBPService {
    WithdrawBikePointDto createWithdrawBikePoint(WithdrawBikePointDto withdrawBikePointDto);
    WithdrawBikePointDto getWithdrawBPById(Long id);
    WithdrawBikePointDto updateWithdrawBPStatus(Long id, String status);
    String deleteWithdrawBP(Long id);
    WithdrawBPResponse getAllWithdrawBP(int pageNo, int pageSize, String sortBy, String sortDir);
    WithdrawBPResponse getAllWithdrawOfBikePoint(Long bid,int pageNo, int pageSize, String sortBy, String sortDir);
    WithdrawBPResponse getWithdrawOfBikePointByStatus(Long bid,String status, int pageNo, int pageSize, String sortBy, String sortDir);
}
