
package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.isDelete = false AND o.id = :orderId")
    Order findExistOrderById(@Param("orderId") Long orderId);

    @Query("SELECT o FROM Order o WHERE o.isDelete = false AND o.user.id = :userId")
    Order findExistOrderByUserId(@Param("userId") Long userId);
}

