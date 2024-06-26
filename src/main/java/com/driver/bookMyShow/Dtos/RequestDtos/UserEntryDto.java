package com.driver.bookMyShow.Dtos.RequestDtos;

import java.time.LocalDate;

import com.driver.bookMyShow.Enums.Gender;
import lombok.Data;

@Data
public class UserEntryDto {

    private String name;

    private LocalDate dob;

    private String address;

  

    private String mobileNo;

 
    private String emailId;

    private Gender gender;

    private String password;
}
