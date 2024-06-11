package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.BikeType;
import com.swdgr6.bikeplatform.model.entity.Brand;
import com.swdgr6.bikeplatform.model.entity.OilProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BikeTypeRepository extends JpaRepository<BikeType, Long> {
    @Query("SELECT b FROM BikeType b WHERE b.isDelete = false")
    Page<BikeType> findAllNotDeleted(Pageable pageable);

    Page<BikeType> findByOilProductsAndIsDeleteFalse(OilProduct oilProduct, Pageable pageable);

}
