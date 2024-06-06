package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
