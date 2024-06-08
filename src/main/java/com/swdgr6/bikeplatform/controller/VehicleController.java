package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.payload.dto.OilProductDto;
import com.swdgr6.bikeplatform.model.payload.dto.VehicleDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.OilProductUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.requestModel.VehicleUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.OilProductsResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.VehicleResponse;
import com.swdgr6.bikeplatform.service.VehicleService;
import com.swdgr6.bikeplatform.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicle")
public class VehicleController {
    private VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {this.vehicleService=vehicleService;}

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody VehicleDto vehicleDto) {
        VehicleDto bt = vehicleService.saveVehicle(vehicleDto);
        return new ResponseEntity<>(bt, HttpStatus.CREATED);
    }

    @GetMapping
    public VehicleResponse getAllVehicles(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return vehicleService.getAllVehicles(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/by-user/{id}")
    public VehicleResponse getAllVehiclesByUser(@PathVariable("id") Long uid, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                          @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return vehicleService.getAllVehiclesOfUser( uid, pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable("id") Long id) {
        VehicleDto vehicleDto = vehicleService.getVehicleById(id);
        return new ResponseEntity<>(vehicleDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable("id") Long id, @RequestBody VehicleUpdatedRequest bt) {
        VehicleDto bt1 = vehicleService.updateVehicle(id, bt);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        String msg = vehicleService.deleteVehicle(id);
        return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }
}
