package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.BikeType;
import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.entity.OilProductPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OilProductPackageRepository extends JpaRepository<OilProductPackage, Long> {
    @Query("SELECT b FROM OilProductPackage b WHERE b.isDelete = false")
    Page<OilProductPackage> findAllNotDeleted(Pageable pageable);

    Page<OilProductPackage> findByOilProductAndIsDeleteFalse(OilProduct oilProduct, Pageable pageable);

    @Query("SELECT b FROM OilProductPackage b WHERE b.id = :oilpackId AND b.isDelete = false")
    OilProductPackage findExistByOilProduct(Long oilpackId);
}
