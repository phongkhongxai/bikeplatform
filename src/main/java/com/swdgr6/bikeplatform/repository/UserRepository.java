package com.swdgr6.bikeplatform.repository;


import com.swdgr6.bikeplatform.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE u.is_delete = false and u.user_id = :userId", nativeQuery = true)
    User findExistUserById(Long userId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isDelete = false")
    Long countActiveUsers();
}
