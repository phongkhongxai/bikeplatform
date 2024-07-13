package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.BikePoint;
import com.swdgr6.bikeplatform.model.entity.Order;
import com.swdgr6.bikeplatform.model.entity.Transaction;
import com.swdgr6.bikeplatform.model.entity.WithdrawBikePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WithdrawBPRepository extends JpaRepository<WithdrawBikePoint, Long> {
    @Query("SELECT b FROM WithdrawBikePoint b WHERE b.isDelete = false")
    Page<WithdrawBikePoint> findAllNotDeleted(Pageable pageable);

    Page<WithdrawBikePoint> findWithdrawBikePointsByBikePointAndIsDeleteFalse(BikePoint bikePoint, Pageable pageable);

    Page<WithdrawBikePoint> findWithdrawBikePointsByBikePointAndIsDeleteFalseAndStatus(BikePoint bikePoint,String status, Pageable pageable);
}
