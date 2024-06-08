package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.BikePointDto;
import com.swdgr6.bikeplatform.model.payload.dto.BikeTypeDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.AnyIdsRequest;
import com.swdgr6.bikeplatform.model.payload.requestModel.BikePointUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikePointsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikeTypesResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;
import com.swdgr6.bikeplatform.service.BikePointService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bike-point")
public class BikePointController {
    private BikePointService bikePointService;
    @Autowired
    public BikePointController(BikePointService bikePointService){this.bikePointService=bikePointService;}

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBikePoint(@RequestBody BikePointDto bikePointDto, @RequestParam(value = "accountNumber") String accountNumber, @RequestParam(value = "bankName") String bankName) {
        BikePointDto bt = bikePointService.saveBikePoint(bikePointDto, accountNumber, bankName);
        return new ResponseEntity<>(bt, HttpStatus.CREATED);
    }

    @GetMapping
    public BikePointsResponse getAllBikePoints(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return bikePointService.getAllBikePoints(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/by-user/{id}")
    public BikePointsResponse getBikePointByUser(@PathVariable("id") Long id, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {

        return bikePointService.getAllBikePointsOfUser(id, pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/by-brand/{id}")
    public BikePointsResponse getBikePointByBrand(@PathVariable("id") Long id, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                 @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                 @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {

        return bikePointService.getAllBikePointsHaveBrand(id,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBikePointById(@PathVariable("id") Long id) {
        BikePointDto bikePointDto = bikePointService.getBikePointById(id);
        return new ResponseEntity<>(bikePointDto, HttpStatus.OK);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBikePoint(@PathVariable("id") Long id, @RequestBody BikePointUpdatedRequest bt) {
        BikePointDto bt1 = bikePointService.updateBikePoint(id, bt);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBikePoint(@PathVariable("id") Long id) {
        String msg = bikePointService.deleteBikePoint(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/brand")
    public ResponseEntity<?> addBrandForBikePoint(@PathVariable("id") Long bikePointId, @RequestBody AnyIdsRequest brandIds){
        String msg = bikePointService.addBrandForBikePoint(bikePointId, brandIds.getAnyTypeIds());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/brand/{bid}")
    public ResponseEntity<?> removeBrandForBikePoint(@PathVariable("id") Long bikePointId, @PathVariable("bid") Long brandId){
        String msg = bikePointService.removeBrandForBikePoint(bikePointId, brandId);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
