package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.entity.BikeType;
import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.payload.dto.BikeTypeDto;
import com.swdgr6.bikeplatform.model.payload.dto.OilProductDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.OilProductUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OilProductService {
    OilProductDto saveOilProduct(OilProductDto oilProductDto);

    OilProductDto getOilProductById(Long id);

    OilProductsResponse getAllOilProducts(int pageNo, int pageSize, String sortBy, String sortDir);

    OilProductDto updateOilProduct(Long id, OilProductUpdatedRequest oilProductDto);

    String deleteOilProduct(Long id);

    String addBikeTypesToOilProduct(Long oilProductId, Set<Long> bikeTypeIds);

    OilProductsResponse getProductsByBikeType(Long bikeTypeId, int pageNo, int pageSize, String sortBy, String sortDir);

    OilProductsResponse getProductsByBrand(Long brandId, int pageNo, int pageSize, String sortBy, String sortDir);


}
