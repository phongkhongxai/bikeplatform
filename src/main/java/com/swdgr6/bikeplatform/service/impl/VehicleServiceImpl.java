package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.BikeType;
import com.swdgr6.bikeplatform.model.entity.User;
import com.swdgr6.bikeplatform.model.entity.Vehicle;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.BikeTypeDto;
import com.swdgr6.bikeplatform.model.payload.dto.VehicleDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.VehicleUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.BikeTypesResponse;
import com.swdgr6.bikeplatform.model.payload.responeModel.VehicleResponse;
import com.swdgr6.bikeplatform.repository.BikeTypeRepository;
import com.swdgr6.bikeplatform.repository.UserRepository;
import com.swdgr6.bikeplatform.repository.VehicleRepository;
import com.swdgr6.bikeplatform.service.VehicleService;
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
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BikeTypeRepository bikeTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public VehicleDto saveVehicle(VehicleDto vehicleDto) {
        Optional<BikeType> bikeTypeOptional = bikeTypeRepository.findById(vehicleDto.getBikeTypeId());
        Optional<User> userOptional = userRepository.findById(vehicleDto.getUserId());

        if (bikeTypeOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Bike Type not found with ID: " + vehicleDto.getBikeTypeId());
        }
        if (userOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "User not found with ID: " + vehicleDto.getUserId());
        }
        Vehicle vehicle = modelMapper.map(vehicleDto, Vehicle.class);
        vehicle.setDelete(false);
        return modelMapper.map(vehicleRepository.save(vehicle), VehicleDto.class);
    }

    @Override
    public VehicleDto getVehicleById(Long id) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);

        if (vehicleOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Vehicle not found with ID: " + id);
        }

        return modelMapper.map(vehicleOptional.get(), VehicleDto.class);
    }

    @Override
    public VehicleResponse getAllVehicles(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Vehicle> vehicles = vehicleRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<Vehicle> listOfBike = vehicles.getContent();

        List<VehicleDto> content = listOfBike.stream().map(bt -> modelMapper.map(bt, VehicleDto.class)).collect(Collectors.toList());

        VehicleResponse templatesResponse = new VehicleResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(vehicles.getNumber());
        templatesResponse.setPageSize(vehicles.getSize());
        templatesResponse.setTotalElements(vehicles.getTotalElements());
        templatesResponse.setTotalPages(vehicles.getTotalPages());
        templatesResponse.setLast(vehicles.isLast());

        return templatesResponse;
    }

    @Override
    public VehicleResponse getAllVehiclesOfUser(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId);
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Vehicle> vehicles = vehicleRepository.findVehiclesByUserAndIsDeleteFalse(userOptional.get(), pageable);

        // get content for page object
        List<Vehicle> listOfBike = vehicles.getContent();

        List<VehicleDto> content = listOfBike.stream().map(bt -> modelMapper.map(bt, VehicleDto.class)).collect(Collectors.toList());

        VehicleResponse templatesResponse = new VehicleResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(vehicles.getNumber());
        templatesResponse.setPageSize(vehicles.getSize());
        templatesResponse.setTotalElements(vehicles.getTotalElements());
        templatesResponse.setTotalPages(vehicles.getTotalPages());
        templatesResponse.setLast(vehicles.isLast());

        return templatesResponse;
    }

    @Override
    public VehicleDto updateVehicle(Long id, VehicleUpdatedRequest vehicleDto) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);

        if (vehicleOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Vehicle not found with ID: " + id);
        }

        Vehicle existingVehicle = vehicleOptional.get();

        if (vehicleDto.getPlate() != null) {
            existingVehicle.setPlate(vehicleDto.getPlate());
        }
        if (vehicleDto.getColor() != null) {
            existingVehicle.setColor(vehicleDto.getColor());
        }
        if (vehicleDto.getBikeTypeId() != null) {
            Optional<BikeType> bikeTypeOptional = bikeTypeRepository.findById(vehicleDto.getBikeTypeId());
            if (bikeTypeOptional.isEmpty()) {
                throw new BikeApiException(HttpStatus.NOT_FOUND, "Bike Type not found with ID: " + vehicleDto.getBikeTypeId());
            }
            existingVehicle.setBikeType(bikeTypeOptional.get());
        }


        return modelMapper.map(vehicleRepository.save(existingVehicle), VehicleDto.class);
    }

    @Override
    public String deleteVehicle(Long id) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        if (vehicleOptional.isEmpty()) {
            throw new BikeApiException(HttpStatus.NOT_FOUND, "Vehicle not found with ID: " + id);
        }
        Vehicle vehicle = vehicleOptional.get();
        vehicle.setDelete(true);
        vehicleRepository.save(vehicle);
        return "Deleted successfully.";
    }
}
