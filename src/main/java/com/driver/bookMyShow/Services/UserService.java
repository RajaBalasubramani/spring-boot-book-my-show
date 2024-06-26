package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.UserEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Enums.RoleName;
import com.driver.bookMyShow.Exceptions.UnderageException;
import com.driver.bookMyShow.Exceptions.UserAlreadyExistsWithEmail;
import com.driver.bookMyShow.Exceptions.UserDoesNotExists;
import com.driver.bookMyShow.Models.Role;
import com.driver.bookMyShow.Models.Ticket;
import com.driver.bookMyShow.Models.AuthUser;
import com.driver.bookMyShow.Repositories.RoleRepository;
import com.driver.bookMyShow.Repositories.UserRepository;
import com.driver.bookMyShow.Transformers.TicketTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.management.relation.RoleNotFoundException;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Transactional
    public AuthUser registerUser(UserEntryDto userDto) throws RoleNotFoundException {
        // Validate age
        if (Period.between(userDto.getDob(), LocalDate.now()).getYears() < 18) {
            throw new UnderageException("User must be at least 18 years old");
        }

        // Create User entity and save
        AuthUser user = new AuthUser();
        user.setName(userDto.getName());
        user.setAddress(userDto.getAddress());
        user.setGender(userDto.getGender());
        user.setEmailId(userDto.getEmailId());
        user.setMobileNo(userDto.getMobileNo());
        user.setDob(userDto.getDob());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

        // Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
        //         .orElseThrow(() -> new RoleNotFoundException("Role not found"));
        user.setRoles(Collections.singleton("USER").toArray(new String[0]));

        return userRepository.save(user);
    }

    public List<TicketResponseDto> allTickets(Integer userId) throws UserDoesNotExists{
        Optional<AuthUser> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) {
            throw new UserDoesNotExists();
        }
        AuthUser user = userOpt.get();
        List<Ticket> ticketList = user.getTicketList();
        List<TicketResponseDto> ticketResponseDtos = new ArrayList<>();
        for(Ticket ticket : ticketList) {
            TicketResponseDto ticketResponseDto = TicketTransformer.returnTicket(ticket.getShow(), ticket);
            ticketResponseDtos.add(ticketResponseDto);
        }
        return ticketResponseDtos;
    }
}
