package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b WHERE b.isDelete = false")
    Page<Brand> findAllNotDeleted(Pageable pageable);
}
