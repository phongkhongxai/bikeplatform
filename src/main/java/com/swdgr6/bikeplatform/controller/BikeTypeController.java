package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.BikeTypeDto;
import com.swdgr6.bikeplatform.model.payload.dto.BrandDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.BikeTypeUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.requestModel.BrandUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikeTypesResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.BrandsResponse;
import com.swdgr6.bikeplatform.service.BikeTypeService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bike-type")
public class BikeTypeController {
    private BikeTypeService bikeTypeService;

    @Autowired
    public BikeTypeController(BikeTypeService bikeTypeService){this.bikeTypeService=bikeTypeService; }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBikeType(@RequestBody BikeTypeDto bikeTypeDto) {
        BikeTypeDto bt = bikeTypeService.saveBikeType(bikeTypeDto);
        return new ResponseEntity<>(bt, HttpStatus.CREATED);
    }

    @GetMapping
    public BikeTypesResponse getAllBikeTypes(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return bikeTypeService.getAllBikeTypes(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBikeTypeById(@PathVariable("id") Long id) {
        BikeTypeDto bikeTypeDto = bikeTypeService.getBikeTypeById(id);
        return new ResponseEntity<>(bikeTypeDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBikeType(@PathVariable("id") Long id, @RequestBody BikeTypeUpdatedRequest bt) {
        BikeTypeDto bt1 = bikeTypeService.updateBikeType(id, bt);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBikeType(@PathVariable("id") Long id) {
        String msg = bikeTypeService.deleteBikeType(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/by-product/{id}")
    public BikeTypesResponse getAllBikeTypesByProduct( @PathVariable("id") Long oid, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                             @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return bikeTypeService.getBikeTypeByProduct( oid,pageNo, pageSize, sortBy, sortDir);
    }


}
