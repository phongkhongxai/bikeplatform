package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
