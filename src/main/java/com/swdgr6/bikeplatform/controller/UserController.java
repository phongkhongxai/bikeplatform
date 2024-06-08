package com.swdgr6.bikeplatform.controller;

import com.swdgr6.bikeplatform.model.entity.User;
import com.swdgr6.bikeplatform.model.payload.dto.UserDTO;
import com.swdgr6.bikeplatform.model.payload.dto.VehicleDto;
import com.swdgr6.bikeplatform.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private ModelMapper modelMapper;
  @GetMapping("/{username-or-email}")
  public ResponseEntity<?> getByUsernameOrEmail(@PathVariable("username-or-email") String usernameOrEmail) {
    System.out.println(usernameOrEmail);
    System.out.println(userService.findByUsernameOrEmail(usernameOrEmail));
    System.out.println(userService.findByUsernameOrEmail(usernameOrEmail).get());
    UserDTO userDTO = modelMapper.map(userService.findByUsernameOrEmail(usernameOrEmail).get(),UserDTO.class);
    System.out.println(userDTO);
    return new ResponseEntity<>(userDTO, HttpStatus.OK);
  }
}
