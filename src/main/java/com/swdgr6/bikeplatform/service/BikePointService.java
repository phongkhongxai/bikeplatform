package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.BikePointDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.BikePointUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikePointsResponse;

import java.util.Set;

public interface BikePointService {
    BikePointDto saveBikePoint(BikePointDto bikePointDto, String accountNumber, String bankName);
    BikePointDto getBikePointById(Long id);
    BikePointsResponse getAllBikePoints(int pageNo, int pageSize, String sortBy, String sortDir);
    BikePointsResponse getAllBikePointsOfUser(Long uid, int pageNo, int pageSize, String sortBy, String sortDir);
    BikePointsResponse getAllBikePointsHaveBrand(Long brandId, int pageNo, int pageSize, String sortBy, String sortDir);
    BikePointDto updateBikePoint(Long id, BikePointUpdatedRequest bikePointUpdatedRequest);
    String deleteBikePoint(Long id);
    String addBrandForBikePoint(Long bikePointId, Set<Long> brandIds);
    String removeBrandForBikePoint(Long bikePointId, Long brandId);



}
