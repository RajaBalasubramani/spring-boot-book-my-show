// package com.driver.bookMyShow.Transformers;

// import com.driver.bookMyShow.Dtos.RequestDtos.UserEntryDto;
// import com.driver.bookMyShow.Dtos.ResponseDtos.ReturnUserDto;
// import com.driver.bookMyShow.Models.AuthUser;

// public class UserTransformer {

//     public static AuthUser userDtoToUser(UserEntryDto userEntryDto) {
//         AuthUser user = AuthUser.builder()
//                 .name(userEntryDto.getName())
//                 .dob(userEntryDto.getDob())
//                 .address(userEntryDto.getAddress())
//                 .gender(userEntryDto.getGender())
//                 .mobileNo(userEntryDto.getMobileNo())
//                 .emailId(userEntryDto.getEmailId())
//                 .build();

//         return user;
//     }

//     public static ReturnUserDto userToUserDto(AuthUser user) {
//         ReturnUserDto userDto = ReturnUserDto.builder()
//                 .name(user.getName())
//                 .address(user.getAddress())
//                 .gender(user.getGender())
//                 .build();

//         return userDto;
//     }
// }
