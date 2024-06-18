
package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.OilProduct;
import com.swdgr6.bikeplatform.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT b FROM Order b WHERE b.isDelete = false")
    Page<Order> findAllNotDeleted(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.isDelete = false AND o.id = :orderId")
    Order findExistOrderById(@Param("orderId") Long orderId);


}

