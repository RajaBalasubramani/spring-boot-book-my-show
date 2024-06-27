package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.UserEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Enums.RoleName;
import com.driver.bookMyShow.Exceptions.UnderageException;
import com.driver.bookMyShow.Exceptions.UserAlreadyExistsWithEmail;
import com.driver.bookMyShow.Exceptions.UserDoesNotExists;
import com.driver.bookMyShow.Models.AuthUser;
import com.driver.bookMyShow.Models.Movie;
import com.driver.bookMyShow.Models.Role;
import com.driver.bookMyShow.Models.Show;
import com.driver.bookMyShow.Models.Theater;
import com.driver.bookMyShow.Models.Ticket;
import com.driver.bookMyShow.Repositories.RoleRepository;
import com.driver.bookMyShow.Repositories.UserRepository;
import com.driver.bookMyShow.Transformers.TicketTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_WhenUserIsUnderage_ShouldThrowUnderageException() {
        // Arrange
        UserEntryDto userEntryDto = new UserEntryDto();
        userEntryDto.setDob(LocalDate.now().minusYears(17));

        // Act & Assert
        assertThrows(UnderageException.class, () -> userService.registerUser(userEntryDto));
        verify(userRepository, never()).save(any(AuthUser.class));
    }

    @Test
    void registerUser_WhenUserIs18OrAbove_ShouldSaveUser() throws RoleNotFoundException {
        // Arrange
        UserEntryDto userEntryDto = new UserEntryDto();
        userEntryDto.setPassword(new BCryptPasswordEncoder().encode("password"));
        userEntryDto.setDob(LocalDate.now().minusYears(18));

        AuthUser savedUser = new AuthUser();
        when(userRepository.save(any(AuthUser.class))).thenReturn(savedUser);

        // Act
        AuthUser result = userService.registerUser(userEntryDto);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser, result);
    }

    @Test
    void allTickets_WhenUserExists_ShouldReturnTicketResponseDtos() throws UserDoesNotExists {
        // Arrange
        Integer userId = 1;

        AuthUser user = new AuthUser();
        user.setId(userId);
        Ticket ticket1 = new Ticket();
        Show show1 = new Show();
        show1.setMovie(new Movie());
        show1.setTheater(new Theater());
        ticket1.setShow(show1);       
        Ticket ticket2 = new Ticket();
        Show show2 = new Show();
        show2.setTheater(new Theater());
        show2.setMovie(new Movie());
        ticket2.setShow(show2);
        List<Ticket> ticketList = Arrays.asList(ticket1, ticket2);
        user.setTicketList(ticketList);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        TicketResponseDto ticketResponseDto1 = new TicketResponseDto();
        TicketResponseDto ticketResponseDto2 = new TicketResponseDto();
        List<TicketResponseDto> expectedResponseDtos = Arrays.asList(ticketResponseDto1, ticketResponseDto2);

        // Act
        List<TicketResponseDto> result = userService.allTickets(userId);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        assertEquals(expectedResponseDtos, result);
    }

    @Test
    void allTickets_WhenUserDoesNotExist_ShouldThrowUserDoesNotExists() {
        // Arrange
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserDoesNotExists.class, () -> userService.allTickets(userId));
        verify(userRepository, times(1)).findById(userId);
    }
}