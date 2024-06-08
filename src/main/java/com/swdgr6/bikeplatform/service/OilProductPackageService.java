package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.payload.dto.OilProductPackageDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.ProductPackageUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductPackageResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;

public interface OilProductPackageService {
    OilProductPackageDto saveOilProductPackage(OilProductPackageDto oilProductPackageDto);
    OilProductPackageDto getPackage(Long id);
    OilProductPackageResponse getAllPackage(int pageNo, int pageSize, String sortBy, String sortDir);
    OilProductPackageResponse getAllPackageOfProduct(Long proId, int pageNo, int pageSize, String sortBy, String sortDir);
    OilProductPackageDto updatePackage(Long id, ProductPackageUpdatedRequest productPackageUpdatedRequest);
    String deletePackage(Long id);
}
