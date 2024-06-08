package com.swdgr6.bikeplatform.service.impl;


import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.entity.OilProductPackage;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.OilProductPackageDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.ProductPackageUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductPackageResponse;
import com.swdgr6.bikeplatform.repository.OilProductPackageRepository;
import com.swdgr6.bikeplatform.repository.OilProductRepository;
import com.swdgr6.bikeplatform.service.OilProductPackageService;
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
public class OilProductPackageServiceImpl implements OilProductPackageService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OilProductRepository oilProductRepository;

    @Autowired
    private OilProductPackageRepository oilProductPackageRepository;


    @Override
    public OilProductPackageDto saveOilProductPackage(OilProductPackageDto oilProductPackageDto) {
        OilProductPackage oilProductPackage = modelMapper.map(oilProductPackageDto, OilProductPackage.class);
        oilProductPackage.setDelete(false);
        OilProduct oilProduct = oilProductRepository.findById(oilProductPackageDto.getOilProductId())
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Product not found with ID: " + oilProductPackageDto.getOilProductId()));
        oilProductPackage.setOilProduct(oilProduct);
        return modelMapper.map(oilProductPackageRepository.save(oilProductPackage), OilProductPackageDto.class);
    }

    @Override
    public OilProductPackageDto getPackage(Long id) {
        Optional<OilProductPackage> oilProductPackage = oilProductPackageRepository.findById(id);
        if(oilProductPackage.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Package not found with ID: " + id);
        }
        return modelMapper.map(oilProductPackage.get(), OilProductPackageDto.class);
    }

    @Override
    public OilProductPackageResponse getAllPackage(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OilProductPackage> oilProductPackages = oilProductPackageRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<OilProductPackage> listOfProduct = oilProductPackages.getContent();

        List<OilProductPackageDto> content = listOfProduct.stream().map(op -> modelMapper.map(op, OilProductPackageDto.class)).collect(Collectors.toList());

        OilProductPackageResponse templatesResponse = new OilProductPackageResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(oilProductPackages.getNumber());
        templatesResponse.setPageSize(oilProductPackages.getSize());
        templatesResponse.setTotalElements(oilProductPackages.getTotalElements());
        templatesResponse.setTotalPages(oilProductPackages.getTotalPages());
        templatesResponse.setLast(oilProductPackages.isLast());

        return templatesResponse;
    }

    @Override
    public OilProductPackageResponse getAllPackageOfProduct(Long proId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Optional<OilProduct> oilProduct = oilProductRepository.findById(proId);
        if(oilProduct.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Oil Product not found with ID: " + proId);
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<OilProductPackage> oilProductPackages = oilProductPackageRepository.findByOilProductAndIsDeleteFalse(oilProduct.get(), pageable);

        // get content for page object
        List<OilProductPackage> listOfProduct = oilProductPackages.getContent();

        List<OilProductPackageDto> content = listOfProduct.stream().map(op -> modelMapper.map(op, OilProductPackageDto.class)).collect(Collectors.toList());

        OilProductPackageResponse templatesResponse = new OilProductPackageResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(oilProductPackages.getNumber());
        templatesResponse.setPageSize(oilProductPackages.getSize());
        templatesResponse.setTotalElements(oilProductPackages.getTotalElements());
        templatesResponse.setTotalPages(oilProductPackages.getTotalPages());
        templatesResponse.setLast(oilProductPackages.isLast());

        return templatesResponse;
    }

    @Override
    public OilProductPackageDto updatePackage(Long id, ProductPackageUpdatedRequest productPackageUpdatedRequest) {
        Optional<OilProductPackage> oilProductPackage = oilProductPackageRepository.findById(id);
        if(oilProductPackage.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Package not found with ID: " + id);
        }
        OilProductPackage existingPackage = oilProductPackage.get();
        existingPackage.setName(productPackageUpdatedRequest.getName() !=null ? productPackageUpdatedRequest.getName() : existingPackage.getName());
        existingPackage.setDescription(productPackageUpdatedRequest.getDescription() !=null ? productPackageUpdatedRequest.getDescription() : existingPackage.getDescription());
        existingPackage.setChangeTimes(productPackageUpdatedRequest.getChangeTimes() !=0  ? productPackageUpdatedRequest.getChangeTimes() : existingPackage.getChangeTimes());
        existingPackage.setKilometerChange(productPackageUpdatedRequest.getKilometerChange() !=0 ? productPackageUpdatedRequest.getKilometerChange() : existingPackage.getKilometerChange());
        existingPackage.setTotalPrice(productPackageUpdatedRequest.getTotalPrice() !=0 ? productPackageUpdatedRequest.getTotalPrice() : existingPackage.getTotalPrice());

        OilProductPackage updatedPackage = oilProductPackageRepository.save(existingPackage);
        return modelMapper.map(updatedPackage, OilProductPackageDto.class);
    }

    @Override
    public String deletePackage(Long id) {
        Optional<OilProductPackage> oilProductPackage = oilProductPackageRepository.findById(id);
        if(oilProductPackage.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Package not found with ID: " + id);
        }
        OilProductPackage existingPackage = oilProductPackage.get();
        existingPackage.setDelete(true);
        oilProductPackageRepository.save(existingPackage);
        return "Deleted package successfully.";
    }
}
