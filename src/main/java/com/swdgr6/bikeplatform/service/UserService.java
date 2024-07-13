package com.swdgr6.bikeplatform.service;


import com.swdgr6.bikeplatform.model.payload.dto.SignupDto;
import com.swdgr6.bikeplatform.model.payload.dto.UserDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.UserUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.UsersResponse;

public interface UserService {

    UserDto saveAdminUser(SignupDto signupDto);
    UserDto saveOwnerUser(SignupDto signupDto);

    UserDto updateUser(Long id, UserUpdatedRequest signupDto);
    UserDto changePassword(Long id, String oldPassword, String newPassword);
    UserDto getProfileUserById(Long id);

    String deleteUser(Long id);
    UsersResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir);

    UsersResponse getAllOwnerUser(int pageNo, int pageSize, String sortBy, String sortDir);
    UsersResponse getAllAdminUser(int pageNo, int pageSize, String sortBy, String sortDir);
    UsersResponse getAllCustomerUser(int pageNo, int pageSize, String sortBy, String sortDir);



}
