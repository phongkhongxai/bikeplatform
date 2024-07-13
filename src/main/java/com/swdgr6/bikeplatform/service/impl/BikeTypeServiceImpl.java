package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.BikeType;
import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.BikeTypeDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.BikeTypeUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikeTypesResponse;
import com.swdgr6.bikeplatform.repository.BikeTypeRepository;
import com.swdgr6.bikeplatform.repository.OilProductRepository;
import com.swdgr6.bikeplatform.service.BikeTypeService;
import com.swdgr6.bikeplatform.service.CloudinaryService;
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
public class BikeTypeServiceImpl implements BikeTypeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BikeTypeRepository bikeTypeRepository;

    @Autowired
    private OilProductRepository oilProductRepository;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Override
    public BikeTypeDto saveBikeType(BikeTypeDto bikeTypeDto) {
        BikeType bikeType = modelMapper.map(bikeTypeDto, BikeType.class);
        if(bikeTypeDto.getFile()!=null){
            bikeType.setImageUrl(cloudinaryService.uploadFile(bikeTypeDto.getFile(), "bikeplat"));
        }else{
            bikeType.setImageUrl("default");
        }
        bikeType.setDelete(false);
        return modelMapper.map(bikeTypeRepository.save(bikeType), BikeTypeDto.class);
    }

    @Override
    public BikeTypeDto getBikeTypeById(Long id) {
        Optional<BikeType> bikeType = bikeTypeRepository.findById(id);
        if(bikeType.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Bike Type not found with ID: " + id);
        }
        return modelMapper.map(bikeType.get(), BikeTypeDto.class);
    }

    @Override
    public BikeTypesResponse getAllBikeTypes(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<BikeType> bikeTypes = bikeTypeRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<BikeType> listOfBikeType = bikeTypes.getContent();

        List<BikeTypeDto> content = listOfBikeType.stream().map(bt -> modelMapper.map(bt, BikeTypeDto.class)).collect(Collectors.toList());

        BikeTypesResponse templatesResponse = new BikeTypesResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(bikeTypes.getNumber());
        templatesResponse.setPageSize(bikeTypes.getSize());
        templatesResponse.setTotalElements(bikeTypes.getTotalElements());
        templatesResponse.setTotalPages(bikeTypes.getTotalPages());
        templatesResponse.setLast(bikeTypes.isLast());

        return templatesResponse;
    }

    @Override
    public BikeTypeDto updateBikeType(Long id, BikeTypeUpdatedRequest bikeTypeDto) {
        Optional<BikeType> bikeType = bikeTypeRepository.findById(id);
        if(bikeType.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Bike Type not found with ID: " + id);
        }
        BikeType existingBikeType = bikeType.get();
        existingBikeType.setBrand(bikeTypeDto.getBrand() !=null ? bikeTypeDto.getBrand() : existingBikeType.getBrand());
        existingBikeType.setModel(bikeTypeDto.getModel() !=null ? bikeTypeDto.getModel() : existingBikeType.getModel());
        existingBikeType.setTransmission(bikeTypeDto.getTransmission() !=null ? bikeTypeDto.getTransmission() : existingBikeType.getTransmission());
        existingBikeType.setOil_capacity(bikeTypeDto.getOilCapacity() !=null ? bikeTypeDto.getOilCapacity() : existingBikeType.getOil_capacity());
        existingBikeType.setCylinder_capacity(bikeTypeDto.getCylinderCapacity() !=null ? bikeTypeDto.getCylinderCapacity() : existingBikeType.getCylinder_capacity());
        if(bikeTypeDto.getFile()!=null){
            existingBikeType.setImageUrl(cloudinaryService.uploadFile(bikeTypeDto.getFile(), "bikeplat"));
        }
        BikeType updatedBikeType = bikeTypeRepository.save(existingBikeType);
        return modelMapper.map(updatedBikeType, BikeTypeDto.class);
    }

    @Override
    public String deleteBikeType(Long id) {
        Optional<BikeType> bikeType = bikeTypeRepository.findById(id);
        if(bikeType.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Bike Type not found with ID: " + id);
        }
        BikeType existingBikeType = bikeType.get();
        existingBikeType.setDelete(true);
        bikeTypeRepository.save(existingBikeType);
        return "Deleted successfully.";
    }

    @Override
    public BikeTypesResponse getBikeTypeByProduct(Long oilId, int pageNo, int pageSize, String sortBy, String sortDir) {
        OilProduct oilProduct = oilProductRepository.findById(oilId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Oil Product not found with id: "+ oilId));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<BikeType> bikeTypes = bikeTypeRepository.findByOilProductsAndIsDeleteFalse(oilProduct, pageable);

        // get content for page object
        List<BikeType> listOfBikeType = bikeTypes.getContent();

        List<BikeTypeDto> content = listOfBikeType.stream().map(bt -> modelMapper.map(bt, BikeTypeDto.class)).collect(Collectors.toList());

        BikeTypesResponse templatesResponse = new BikeTypesResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(bikeTypes.getNumber());
        templatesResponse.setPageSize(bikeTypes.getSize());
        templatesResponse.setTotalElements(bikeTypes.getTotalElements());
        templatesResponse.setTotalPages(bikeTypes.getTotalPages());
        templatesResponse.setLast(bikeTypes.isLast());

        return templatesResponse;
    }
}
