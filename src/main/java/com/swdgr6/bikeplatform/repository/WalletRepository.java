package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT w FROM Wallet w WHERE w.isDeleted = false")
    List<Wallet> findAllNotDeleted();
}
