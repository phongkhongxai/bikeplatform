package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.*;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.BikePointDto;
import com.swdgr6.bikeplatform.model.payload.dto.BikeTypeDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.BikePointUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikePointsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikeTypesResponse;
import com.swdgr6.bikeplatform.repository.*;
import com.swdgr6.bikeplatform.service.BikePointService;
import com.swdgr6.bikeplatform.service.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BikePointServiceImpl implements BikePointService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BikePointRepository bikePointRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public BikePointDto saveBikePoint(BikePointDto bikePointDto, String accountNumber, String bankName) {
        BikePoint bikePoint = modelMapper.map(bikePointDto, BikePoint.class);
        bikePoint.setDelete(false);
        Wallet wallet = new Wallet();
        wallet.setAccountNumber(accountNumber);
        wallet.setBalance(0);
        wallet.setBankName(bankName);
        bikePoint.setWallet(wallet);
        wallet.setBikePoint(bikePoint);
        if(bikePointDto.getFile()!=null){
            bikePoint.setImageUrl(cloudinaryService.uploadFile(bikePointDto.getFile(), "bikeplat"));
        }else{
            bikePoint.setImageUrl("default");
        }

        return modelMapper.map(bikePointRepository.save(bikePoint), BikePointDto.class);
    }

    @Override
    public BikePointDto getBikePointById(Long id) {
        Optional<BikePoint> bikePoint = bikePointRepository.findById(id);
        if(bikePoint.isEmpty()){
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Bike Point not found with id: "+id);
        }
        return modelMapper.map(bikePoint.get(), BikePointDto.class);
    }

    @Override
    public BikePointsResponse getAllBikePoints(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<BikePoint> bikePoints = bikePointRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<BikePoint> listOfBikePoint = bikePoints.getContent();

        List<BikePointDto> content = listOfBikePoint.stream().map(bt -> modelMapper.map(bt, BikePointDto.class)).collect(Collectors.toList());

        BikePointsResponse templatesResponse = new BikePointsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(bikePoints.getNumber());
        templatesResponse.setPageSize(bikePoints.getSize());
        templatesResponse.setTotalElements(bikePoints.getTotalElements());
        templatesResponse.setTotalPages(bikePoints.getTotalPages());
        templatesResponse.setLast(bikePoints.isLast());

        return templatesResponse;
    }

    @Override
    public BikePointsResponse getAllBikePointsOfUser(Long uid, int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"User not found with id: "+ uid));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<BikePoint> bikePoints = bikePointRepository.findByUserAndIsDeleteFalse( user, pageable);

        // get content for page object
        List<BikePoint> listOfBikePoint = bikePoints.getContent();

        List<BikePointDto> content = listOfBikePoint.stream().map(bt -> modelMapper.map(bt, BikePointDto.class)).collect(Collectors.toList());

        BikePointsResponse templatesResponse = new BikePointsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(bikePoints.getNumber());
        templatesResponse.setPageSize(bikePoints.getSize());
        templatesResponse.setTotalElements(bikePoints.getTotalElements());
        templatesResponse.setTotalPages(bikePoints.getTotalPages());
        templatesResponse.setLast(bikePoints.isLast());

        return templatesResponse;
    }

    @Override
    public BikePointsResponse getAllBikePointsHaveBrand(Long brandId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Brand not found with id: "+ brandId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<BikePoint> bikePoints = bikePointRepository.findByBrandsAndIsDeleteFalse(brand, pageable);

        // get content for page object
        List<BikePoint> listOfBikePoint = bikePoints.getContent();

        List<BikePointDto> content = listOfBikePoint.stream().map(bt -> modelMapper.map(bt, BikePointDto.class)).collect(Collectors.toList());

        BikePointsResponse templatesResponse = new BikePointsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(bikePoints.getNumber());
        templatesResponse.setPageSize(bikePoints.getSize());
        templatesResponse.setTotalElements(bikePoints.getTotalElements());
        templatesResponse.setTotalPages(bikePoints.getTotalPages());
        templatesResponse.setLast(bikePoints.isLast());

        return templatesResponse;
    }

    @Override
    public BikePointDto updateBikePoint(Long id, BikePointUpdatedRequest bikePointUpdatedRequest) {
        Optional<BikePoint> bikePoint = bikePointRepository.findById(id);
        if(bikePoint.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Bike Point not found with ID: " + id);
        }
        BikePoint existingBikePoint = bikePoint.get();
        existingBikePoint.setName(bikePointUpdatedRequest.getName() !=null ? bikePointUpdatedRequest.getName() : existingBikePoint.getName());
        existingBikePoint.setAddress(bikePointUpdatedRequest.getAddress() !=null ? bikePointUpdatedRequest.getAddress() : existingBikePoint.getAddress());
        existingBikePoint.setPhone(bikePointUpdatedRequest.getPhone() !=null ? bikePointUpdatedRequest.getPhone() : existingBikePoint.getPhone());
        if(bikePointUpdatedRequest.getFile()!=null){
            existingBikePoint.setImageUrl(cloudinaryService.uploadFile(bikePointUpdatedRequest.getFile(), "bikeplat"));
        }

        BikePoint updatedBikePoint = bikePointRepository.save(existingBikePoint);
        return modelMapper.map(updatedBikePoint, BikePointDto.class);
    }

    @Override
    public String deleteBikePoint(Long id) {
        Optional<BikePoint> bikePoint = bikePointRepository.findById(id);
        if(bikePoint.isEmpty()){
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Bike Point not found with ID: " + id);
        }
        BikePoint existingBikePoint = bikePoint.get();
        existingBikePoint.setDelete(true);
        BikePoint updatedBikePoint = bikePointRepository.save(existingBikePoint);

        return "Deleted BikePoint Successfully.";
    }

    @Override
    public String addBrandForBikePoint(Long bikePointId, Set<Long> brandIds) {
        BikePoint bikePoint = bikePointRepository.findById(bikePointId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Bike Point not found with id: "+bikePointId));

        Set<Brand> brands = new HashSet<>();
        for (Long brandId : brandIds) {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND,"Brand not found with id: "+brandId));
            brands.add(brand);
        }
        if(!brands.isEmpty()){
            bikePoint.setBrands(brands);
            bikePointRepository.save(bikePoint);
            return "Added successfully";
        }
        return "Fail";
    }

    @Override
    public String removeBrandForBikePoint(Long bikePointId, Long brandId) {
        BikePoint bikePoint = bikePointRepository.findById(bikePointId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Bike Point not found with id: " + bikePointId));

        Brand brandToRemove = brandRepository.findById(brandId)
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "Brand not found with id: " + brandId));

        if (bikePoint.getBrands().remove(brandToRemove)) {
            bikePointRepository.save(bikePoint);
            return "Brand removed successfully";
        } else {
            return "Brand was not associated with the BikePoint";
        }
    }
}
