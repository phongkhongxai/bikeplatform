package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.entity.Brand;
import com.swdgr6.bikeplatform.model.payload.dto.BrandDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.BrandUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BrandsResponse;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    BrandDto saveBrand(BrandDto brand);

    BrandDto getBrandById(Long id);

    BrandsResponse getAllBrands(int pageNo, int pageSize, String sortBy, String sortDir);

    BrandDto updateBrand(Long id, BrandUpdatedRequest brand);

    String deleteBrand(Long id);
}
