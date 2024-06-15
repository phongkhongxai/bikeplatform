package com.swdgr6.bikeplatform.model.payload.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private LocalDate dob;
    private String address;
    private String gender;
    private String phone;
    private String username;
    private String avatarUrl;
    private Long roleId;
}
