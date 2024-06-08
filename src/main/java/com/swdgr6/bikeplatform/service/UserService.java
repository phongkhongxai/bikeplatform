package com.swdgr6.bikeplatform.service;

import com.swdgr6.bikeplatform.model.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
  List<User> getAllUser();
  List<User> findByEmail();
  User findExistUserById(Long userId);
  Optional<User> findByUsernameOrEmail(String usernameOrEmail);
}
