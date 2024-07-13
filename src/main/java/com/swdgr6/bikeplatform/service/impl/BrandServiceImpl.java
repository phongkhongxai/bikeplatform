package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.Brand;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.BrandDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.BrandUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BrandsResponse;
import com.swdgr6.bikeplatform.repository.BrandRepository;
import com.swdgr6.bikeplatform.service.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public BrandDto saveBrand(BrandDto brand) {
        Brand brandEntity = modelMapper.map(brand, Brand.class);
        brandEntity.setDelete(false);
        return modelMapper.map(brandRepository.save(brandEntity), BrandDto.class);
    }

    @Override
    public BrandDto getBrandById(Long id) {
        Optional<Brand> brandEntity = brandRepository.findById(id);
        if (brandEntity.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + id);
        }
        return modelMapper.map(brandEntity.get(), BrandDto.class);
    }

    @Override
    public BrandsResponse getAllBrands(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Brand> brands = brandRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<Brand> listOfBrand = brands.getContent();

        List<BrandDto> content= listOfBrand.stream().map(brand -> modelMapper.map(brand, BrandDto.class)).collect(Collectors.toList());

        BrandsResponse templatesResponse = new BrandsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(brands.getNumber());
        templatesResponse.setPageSize(brands.getSize());
        templatesResponse.setTotalElements(brands.getTotalElements());
        templatesResponse.setTotalPages(brands.getTotalPages());
        templatesResponse.setLast(brands.isLast());

        return templatesResponse;
    }

    @Override
    public BrandDto updateBrand(Long id, BrandUpdatedRequest brand) {
        Optional<Brand> brandEntity = brandRepository.findById(id);
        if (brandEntity.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + id);
        }
        Brand existingBrand = brandEntity.get();
        existingBrand.setName(brand.getName() != null ? brand.getName() : existingBrand.getName());
        existingBrand.setEmail(brand.getEmail() != null ? brand.getEmail() : existingBrand.getEmail());
        existingBrand.setAddress(brand.getAddress() != null ? brand.getAddress() : existingBrand.getAddress());
        existingBrand.setPhone(brand.getPhone() !=null ? brand.getPhone() : existingBrand.getPhone());

        Brand updatedBrand = brandRepository.save(existingBrand);
        return modelMapper.map(updatedBrand, BrandDto.class);
    }

    @Override
    public String deleteBrand(Long id) {
        Optional<Brand> brandEntity = brandRepository.findById(id);
        if (brandEntity.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + id);
        }
        Brand existingBrand = brandEntity.get();
        existingBrand.setDelete(true);
        brandRepository.save(existingBrand);
        return "Deleted successfully.";
    }
}
