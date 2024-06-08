package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BikePointRepository extends JpaRepository<BikePoint, Long> {
    @Query("SELECT b FROM BikePoint b WHERE b.isDelete = false")
    Page<BikePoint> findAllNotDeleted(Pageable pageable);

    Page<BikePoint> findByUserAndIsDeleteFalse(User user, Pageable pageable);

    Page<BikePoint> findByBrandsAndIsDeleteFalse(Brand brand, Pageable pageable);

}
