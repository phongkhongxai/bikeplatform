package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.BikeType;
import com.swdgr6.bikeplatform.model.entity.Brand;
import com.swdgr6.bikeplatform.model.entity.OilProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OilProductRepository extends JpaRepository<OilProduct, Long> {
    @Query("SELECT b FROM OilProduct b WHERE b.isDelete = false")
    Page<OilProduct> findAllNotDeleted(Pageable pageable);

    Page<OilProduct> findByBikeTypesAndIsDeleteFalse(BikeType bikeType, Pageable pageable);

    Page<OilProduct> findByBrandAndIsDeleteFalse(Brand brand, Pageable pageable);

}
