package com.swdgr6.bikeplatform.model.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swdgr6.bikeplatform.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDTO {
  private Long id;
  private String email;
  private String password;
  private String fullName;
//  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dob;
  private String address;
  private String gender;
  private String phone;

}
