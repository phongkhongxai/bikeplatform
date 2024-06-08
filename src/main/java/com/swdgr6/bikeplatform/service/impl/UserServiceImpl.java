package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.User;
import com.swdgr6.bikeplatform.model.payload.dto.UserDTO;
import com.swdgr6.bikeplatform.repository.UserRepository;
import com.swdgr6.bikeplatform.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;
  @Override
  public List<User> getAllUser() {
    return null;
  }

  @Override
  public List<User> findByEmail() {
    return null;
  }

  @Override
  public User findExistUserById(Long userId) {
    return null;
  }

  @Override
  public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
    return  userRepository.findByUserNameOrEmail(usernameOrEmail);
  }



}
