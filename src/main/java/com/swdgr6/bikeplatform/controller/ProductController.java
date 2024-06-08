package com.swdgr6.bikeplatform.controller;


import com.swdgr6.bikeplatform.model.payload.dto.OilProductDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.AnyIdsRequest;
import com.swdgr6.bikeplatform.model.payload.requestModel.OilProductUpdatedRequest;

import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;
import com.swdgr6.bikeplatform.service.OilProductService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private OilProductService oilProductService;

    @Autowired
    public ProductController(OilProductService oilProductService){
        this.oilProductService = oilProductService;
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody OilProductDto oilProductDto) {
        OilProductDto bt = oilProductService.saveOilProduct(oilProductDto);
        return new ResponseEntity<>(bt, HttpStatus.CREATED);
    }

    @GetMapping
    public OilProductsResponse getAllProducts(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return oilProductService.getAllOilProducts(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        OilProductDto oilProductDto = oilProductService.getOilProductById(id);
        return new ResponseEntity<>(oilProductDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody OilProductUpdatedRequest bt) {
        OilProductDto bt1 = oilProductService.updateOilProduct(id, bt);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        String msg = oilProductService.deleteOilProduct(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/bike-types")
    public ResponseEntity<?> addBikeTypeForProduct(@PathVariable("id") Long oilid, @RequestBody AnyIdsRequest bikeTypeIds){
        String msg = oilProductService.addBikeTypesToOilProduct(oilid, bikeTypeIds.getAnyTypeIds());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/by-bike-type/{id}")
    public OilProductsResponse getProductByBikeType(@PathVariable("id") Long id, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {

        return oilProductService.getProductsByBikeType(id,pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/by-brand/{id}")
    public OilProductsResponse getProductByBrand(@PathVariable("id") Long id, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {

        return oilProductService.getProductsByBrand(id,pageNo, pageSize, sortBy, sortDir);
    }

}
