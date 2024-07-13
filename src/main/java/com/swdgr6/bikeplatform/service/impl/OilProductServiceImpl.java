package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.BikeType;
import com.swdgr6.bikeplatform.model.entity.Brand;
import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.BikeTypeDto;
import com.swdgr6.bikeplatform.model.payload.dto.OilProductDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.OilProductUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikeTypesResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;
import com.swdgr6.bikeplatform.repository.BikeTypeRepository;
import com.swdgr6.bikeplatform.repository.BrandRepository;
import com.swdgr6.bikeplatform.repository.OilProductRepository;
import com.swdgr6.bikeplatform.service.CloudinaryService;
import com.swdgr6.bikeplatform.service.OilProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class OilProductServiceImpl implements OilProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OilProductRepository oilProductRepository;

    @Autowired
    private BikeTypeRepository bikeTypeRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Override
    public OilProductDto saveOilProduct(OilProductDto oilProductDto) {
        OilProduct oilProduct = modelMapper.map(oilProductDto, OilProduct.class);
        oilProduct.setDelete(false);
        Brand brand = brandRepository.findById(oilProductDto.getBrandId())
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + oilProductDto.getBrandId()));

        oilProduct.setBrand(brand);
        if(oilProductDto.getFile()!=null){
            oilProduct.setImageUrl(cloudinaryService.uploadFile(oilProductDto.getFile(), "bikeplat"));
        }else{
            oilProduct.setImageUrl("default");
        }
        return modelMapper.map(oilProductRepository.save(oilProduct), OilProductDto.class);
    }

    @Override
    public OilProductDto getOilProductById(Long id) {
        Optional<OilProduct> oilProduct = oilProductRepository.findById(id);
        if(oilProduct.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Oil Product not found with ID: " + id);
        }
        return modelMapper.map(oilProduct.get(), OilProductDto.class);
    }

    @Override
    public OilProductsResponse getAllOilProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OilProduct> oilProducts = oilProductRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<OilProduct> listOfProduct = oilProducts.getContent();

        List<OilProductDto> content = listOfProduct.stream().map(op -> modelMapper.map(op, OilProductDto.class)).collect(Collectors.toList());

        OilProductsResponse templatesResponse = new OilProductsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(oilProducts.getNumber());
        templatesResponse.setPageSize(oilProducts.getSize());
        templatesResponse.setTotalElements(oilProducts.getTotalElements());
        templatesResponse.setTotalPages(oilProducts.getTotalPages());
        templatesResponse.setLast(oilProducts.isLast());

        return templatesResponse;
    }

    @Override
    public OilProductDto updateOilProduct(Long id, OilProductUpdatedRequest oilProductDto) {
        Optional<OilProduct> oilProduct = oilProductRepository.findById(id);
        if(oilProduct.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Oil Product not found with ID: " + id);
        }
        OilProduct existingOilProduct = oilProduct.get();
        existingOilProduct.setName(oilProductDto.getName() !=null ? oilProductDto.getName() : existingOilProduct.getName());
        existingOilProduct.setDescription(oilProductDto.getDescription() !=null ? oilProductDto.getDescription() : existingOilProduct.getDescription());
        existingOilProduct.setPrice(oilProductDto.getPrice() !=0  ? oilProductDto.getPrice() : existingOilProduct.getPrice());
        existingOilProduct.setTechStand(oilProductDto.getTechStand() !=null ? oilProductDto.getTechStand() : existingOilProduct.getTechStand());
        existingOilProduct.setOilType(oilProductDto.getOilType() !=null ? oilProductDto.getOilType() : existingOilProduct.getOilType());
        if(oilProductDto.getBrandId() != null){
            Brand brand = brandRepository.findById(oilProductDto.getBrandId())
                    .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + oilProductDto.getBrandId()));
            existingOilProduct.setBrand(brand);
        }
        if(oilProductDto.getFile()!=null){
            existingOilProduct.setImageUrl(cloudinaryService.uploadFile(oilProductDto.getFile(), "bikeplat"));
        }
        OilProduct updatedOilProduct = oilProductRepository.save(existingOilProduct);
        return modelMapper.map(updatedOilProduct, OilProductDto.class);
    }

    @Override
    public String deleteOilProduct(Long id) {
        Optional<OilProduct> oilProduct = oilProductRepository.findById(id);
        if(oilProduct.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Oil Product not found with ID: " + id);
        }
        OilProduct existingOilProduct = oilProduct.get();
        existingOilProduct.setDelete(true);
        oilProductRepository.save(existingOilProduct);
        return "Deleted successfully.";

    }

    @Override
    public String addBikeTypesToOilProduct(Long oilProductId, Set<Long> bikeTypeIds) {
        OilProduct oilProduct = oilProductRepository.findById(oilProductId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"OilProduct not found with id: "+oilProductId));

        Set<BikeType> bikeTypes = new HashSet<>();
        for (Long bikeTypeId : bikeTypeIds) {
            BikeType bikeType = bikeTypeRepository.findById(bikeTypeId)
                    .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Bike Type not found with id: "+bikeTypeId));
            bikeTypes.add(bikeType);
        }
        if(!bikeTypes.isEmpty()){
            oilProduct.setBikeTypes(bikeTypes);
            oilProductRepository.save(oilProduct);
            return "Added successfully";
        }
        return "Fail";
    }

    @Override
    public OilProductsResponse getProductsByBikeType(Long bikeTypeId, int pageNo, int pageSize, String sortBy, String sortDir) {
        BikeType bikeType = bikeTypeRepository.findById(bikeTypeId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Bike Type not found with id: "+ bikeTypeId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OilProduct> oilProducts = oilProductRepository.findByBikeTypesAndIsDeleteFalse(bikeType, pageable);

        // get content for page object
        List<OilProduct> listOfProduct = oilProducts.getContent();

        List<OilProductDto> content = listOfProduct.stream().map(op -> modelMapper.map(op, OilProductDto.class)).collect(Collectors.toList());

        OilProductsResponse templatesResponse = new OilProductsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(oilProducts.getNumber());
        templatesResponse.setPageSize(oilProducts.getSize());
        templatesResponse.setTotalElements(oilProducts.getTotalElements());
        templatesResponse.setTotalPages(oilProducts.getTotalPages());
        templatesResponse.setLast(oilProducts.isLast());

        return templatesResponse;

    }

    @Override
    public OilProductsResponse getProductsByBrand(Long brandId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Brand not found with id: "+ brandId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OilProduct> oilProducts = oilProductRepository.findByBrandAndIsDeleteFalse(brand, pageable);

        // get content for page object
        List<OilProduct> listOfProduct = oilProducts.getContent();

        List<OilProductDto> content = listOfProduct.stream().map(op -> modelMapper.map(op, OilProductDto.class)).collect(Collectors.toList());

        OilProductsResponse templatesResponse = new OilProductsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(oilProducts.getNumber());
        templatesResponse.setPageSize(oilProducts.getSize());
        templatesResponse.setTotalElements(oilProducts.getTotalElements());
        templatesResponse.setTotalPages(oilProducts.getTotalPages());
        templatesResponse.setLast(oilProducts.isLast());

        return templatesResponse;
    }
}
