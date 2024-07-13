package com.swdgr6.bikeplatform.repository;

import com.swdgr6.bikeplatform.model.entity.Transaction;
import com.swdgr6.bikeplatform.model.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT w FROM Wallet w WHERE w.isDelete = false")
    List<Wallet> findAllNotDeleted();

    @Query("SELECT b FROM Wallet b WHERE b.isDelete = false")
    Page<Transaction> findAllNotDeleted(Pageable pageable);
}
