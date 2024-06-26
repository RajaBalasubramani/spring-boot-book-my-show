package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.UserEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Models.Ticket;
import com.driver.bookMyShow.Models.AuthUser;
import com.driver.bookMyShow.Services.UserService;

import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.management.relation.RoleNotFoundException;
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
        public ResponseEntity<?> registerUser(@RequestBody UserEntryDto userDto) {
        try {
            userService.registerUser(userDto);
        } catch (RoleNotFoundException e) {
            return new ResponseEntity<>(ResponseEntity.status(400), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse().description("User Registered Succesfully"));
    }


    @PostMapping("/registeradmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdminUser(@Valid @RequestBody UserEntryDto userDto) {
        try {
            userService.registerUser(userDto);
        } catch (RoleNotFoundException e) {
            return new ResponseEntity<>(ResponseEntity.status(400), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse().description("User Registered Succesfully"));
    }

    @GetMapping("/allTickets/{userId}")
    public ResponseEntity<List<TicketResponseDto>> allTickets(@PathVariable Integer userId) {
        try {
            List<TicketResponseDto> result = userService.allTickets(userId);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
