package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.BikePoint;
import com.swdgr6.bikeplatform.model.entity.Order;
import com.swdgr6.bikeplatform.model.entity.OrderUsing;
import com.swdgr6.bikeplatform.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT b FROM Transaction b WHERE b.isDelete = false")
    Page<Transaction> findAllNotDeleted(Pageable pageable);

    Page<Transaction> findTransactionsByOrderUsingBikePointAndIsDeleteFalse(BikePoint bikePoint, Pageable pageable);

    Page<Transaction> findTransactionsByOrderUsingBikePointAndIsDeleteFalseAndStatus(BikePoint bikePoint,String status, Pageable pageable);

}
