package com.swdgr6.bikeplatform.controller;


import com.swdgr6.bikeplatform.model.payload.dto.BrandDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.BrandUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BrandsResponse;
import com.swdgr6.bikeplatform.service.BrandService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {
    private BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody BrandDto brandDto) {
        BrandDto brandDto1 = brandService.saveBrand(brandDto);
        return new ResponseEntity<>(brandDto1, HttpStatus.CREATED);
    }

    @GetMapping
    public BrandsResponse getAllBrands(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                       @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                       @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return brandService.getAllBrands(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable("id") Long id) {
        BrandDto brandDto = brandService.getBrandById(id);
        return new ResponseEntity<>(brandDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable("id") Long id, @Valid @RequestBody BrandUpdatedRequest brandDto) {
        BrandDto brandDto1 = brandService.updateBrand(id, brandDto);
        return new ResponseEntity<>(brandDto1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable("id") Long id) {
        String msg = brandService.deleteBrand(id);
        return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }

}
