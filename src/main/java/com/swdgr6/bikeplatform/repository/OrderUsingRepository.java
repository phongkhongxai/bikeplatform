package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.BikePoint;
import com.swdgr6.bikeplatform.model.entity.Order;
import com.swdgr6.bikeplatform.model.entity.OrderUsing;
import com.swdgr6.bikeplatform.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


public interface OrderUsingRepository extends JpaRepository<OrderUsing, Long> {

    @Query("SELECT b FROM OrderUsing b WHERE b.isDelete = false")
    Page<OrderUsing> findAllNotDeleted(Pageable pageable);

    Page<OrderUsing> findOrderUsingsByBikePointAndIsDeleteFalseAndIsConfirmTrue(BikePoint bikePoint, Pageable pageable);
    Page<OrderUsing> findOrderUsingsByBikePointAndIsDeleteFalseAndIsConfirmFalse(BikePoint bikePoint, Pageable pageable);
    Page<OrderUsing> findOrderUsingsByOrderAndIsDeleteFalse(Order order, Pageable pageable);

    Page<OrderUsing> findOrderUsingsByStatusAndIsDeleteFalse(String status, Pageable pageable);
    Page<OrderUsing> findOrderUsingsByBikePointAndIsDeleteFalseAndStatus(BikePoint bikePoint, String status, Pageable pageable);
    Page<OrderUsing> findOrderUsingsByBikePointAndIsDeleteFalseAndIsConfirmFalseAndStatus(BikePoint bikePoint, String status, Pageable pageable);

    Page<OrderUsing> findOrderUsingsByOrder_UserAndIsDeleteFalse(User user, Pageable pageable);
    Page<OrderUsing> findOrderUsingsByOrder_UserAndIsDeleteFalseAndStatus(User user, String status, Pageable pageable);

}
