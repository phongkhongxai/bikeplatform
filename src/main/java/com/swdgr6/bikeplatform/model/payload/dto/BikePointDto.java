package com.swdgr6.bikeplatform.model.payload.dto;


import com.swdgr6.bikeplatform.model.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BikePointDto {
    private Long id;
    @NotEmpty(message = "Name should not be empty!")
    @Size(max = 255, message = "Name should not exceed 255 characters!")
    private String name;

    @NotEmpty(message = "Address should not be empty!")
    @Size(max = 255, message = "Address should not exceed 255 characters!")
    private String address;

    @NotEmpty(message = "Phone should not be empty!")
    @Pattern(regexp="(^$|[0-9]{8,11})", message = "Phone number must be 8 or 11 digits!")
    private String phone;
    private Long walletId;
    @NotNull(message = "User should not be null!")
    private Long userId;
}
