package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.entity.Vehicle;
import com.swdgr6.bikeplatform.model.payload.dto.VehicleDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.VehicleUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.VehicleResponse;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    VehicleDto saveVehicle(VehicleDto vehicleDto);

    VehicleDto getVehicleById(Long id);

    VehicleResponse getAllVehicles(int pageNo, int pageSize, String sortBy, String sortDir);

    VehicleResponse getAllVehiclesOfUser(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    VehicleDto updateVehicle(Long id, VehicleUpdatedRequest vehicleDto);

    String deleteVehicle(Long id);
}
