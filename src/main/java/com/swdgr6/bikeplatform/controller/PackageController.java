package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.OilProductDto;
import com.swdgr6.bikeplatform.model.payload.dto.OilProductPackageDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.OilProductUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.requestModel.ProductPackageUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductPackageResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;
import com.swdgr6.bikeplatform.service.OilProductPackageService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/package")
public class PackageController {
    private OilProductPackageService oilProductPackageService;

    @Autowired
    public PackageController(OilProductPackageService oilProductPackageService){this.oilProductPackageService=oilProductPackageService;}

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPackage(@RequestBody OilProductPackageDto oilProductPackageDto) {
        OilProductPackageDto bt = oilProductPackageService.saveOilProductPackage(oilProductPackageDto);
        return new ResponseEntity<>(bt, HttpStatus.CREATED);
    }

    @GetMapping
    public OilProductPackageResponse getAllPackages(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return oilProductPackageService.getAllPackage(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("by-product/{proid}")
    public OilProductPackageResponse getAllPackagesByProduct(@PathVariable("proid") Long proid, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return oilProductPackageService.getAllPackageOfProduct(proid, pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable("id") Long id) {
        OilProductPackageDto oilProductPackageDto = oilProductPackageService.getPackage(id);
        return new ResponseEntity<>(oilProductPackageDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackage(@PathVariable("id") Long id, @RequestBody ProductPackageUpdatedRequest bt) {
        OilProductPackageDto bt1 = oilProductPackageService.updatePackage(id, bt);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable("id") Long id) {
        String msg = oilProductPackageService.deletePackage(id);
        return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }

}
