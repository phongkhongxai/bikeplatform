package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.entity.BikeType;
import com.swdgr6.bikeplatform.model.payload.dto.BikeTypeDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.BikeTypeUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikeTypesResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;

import java.util.List;
import java.util.Optional;

public interface BikeTypeService {
    BikeTypeDto saveBikeType(BikeTypeDto bikeTypeDto);

    BikeTypeDto getBikeTypeById(Long id);

    BikeTypesResponse getAllBikeTypes(int pageNo, int pageSize, String sortBy, String sortDir);

    BikeTypeDto updateBikeType(Long id, BikeTypeUpdatedRequest bikeTypeDto);

    String deleteBikeType(Long id);

    BikeTypesResponse getBikeTypeByProduct(Long oilId, int pageNo, int pageSize, String sortBy, String sortDir);

}
