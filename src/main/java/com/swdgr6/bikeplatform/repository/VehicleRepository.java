package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.entity.User;
import com.swdgr6.bikeplatform.model.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("SELECT ve FROM Vehicle ve WHERE ve.isDelete = false")
    Page<Vehicle> findAllNotDeleted(Pageable pageable);

    Page<Vehicle> findVehiclesByUserAndIsDeleteFalse(User user, Pageable pageable);
}
