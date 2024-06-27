package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.TicketEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Exceptions.RequestedSeatAreNotAvailable;
import com.driver.bookMyShow.Exceptions.ShowDoesNotExists;
import com.driver.bookMyShow.Exceptions.UserDoesNotExists;
import com.driver.bookMyShow.Models.AuthUser;
import com.driver.bookMyShow.Models.Show;
import com.driver.bookMyShow.Models.ShowSeat;
import com.driver.bookMyShow.Models.Ticket;
import com.driver.bookMyShow.Repositories.ShowRepository;
import com.driver.bookMyShow.Repositories.TheaterRepository;
import com.driver.bookMyShow.Repositories.TicketRepository;
import com.driver.bookMyShow.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ticketBooking_WhenShowDoesNotExist_ShouldThrowShowDoesNotExists() {
        // Arrange
        TicketEntryDto ticketEntryDto = new TicketEntryDto();
        ticketEntryDto.setShowId(1);

        when(showRepository.findById(ticketEntryDto.getShowId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ShowDoesNotExists.class, () -> ticketService.ticketBooking(ticketEntryDto));
        verify(showRepository, times(1)).findById(ticketEntryDto.getShowId());
        verify(userRepository, never()).findById(1);
        verify(ticketRepository, never()).save(any());
        verify(userRepository, never()).save(any());
        verify(showRepository, never()).save(any());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void ticketBooking_WhenUserDoesNotExist_ShouldThrowUserDoesNotExists() {
        // Arrange
        TicketEntryDto ticketEntryDto = new TicketEntryDto();
        ticketEntryDto.setShowId(11);
        ticketEntryDto.setUserId(11);

        Show show = new Show();
        when(showRepository.findById(ticketEntryDto.getShowId())).thenReturn(Optional.of(show));
        when(userRepository.findById(ticketEntryDto.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserDoesNotExists.class, () -> ticketService.ticketBooking(ticketEntryDto));
        verify(showRepository, times(1)).findById(ticketEntryDto.getShowId());
        verify(userRepository, times(1)).findById(ticketEntryDto.getUserId());
        verify(ticketRepository, never()).save(any());
        verify(userRepository, never()).save(any());
        verify(showRepository, never()).save(any());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void ticketBooking_WhenRequestedSeatsAreNotAvailable_ShouldThrowRequestedSeatAreNotAvailable() {
        // Arrange
        TicketEntryDto ticketEntryDto = new TicketEntryDto();
        ticketEntryDto.setShowId(11);
        ticketEntryDto.setUserId(11);
        ticketEntryDto.setRequestSeats(List.of("A1", "A2"));

        Show show = new Show();
        AuthUser user = new AuthUser();
        when(showRepository.findById(ticketEntryDto.getShowId())).thenReturn(Optional.of(show));
        when(userRepository.findById(ticketEntryDto.getUserId())).thenReturn(Optional.of(user));

        // Set up show seat list
        List<ShowSeat> showSeatList = new ArrayList<>();
        ShowSeat showSeat1 = new ShowSeat();
        showSeat1.setSeatNo("A1");
        showSeat1.setIsAvailable(false);
        showSeatList.add(showSeat1);
        ShowSeat showSeat2 = new ShowSeat();
        showSeat2.setSeatNo("A2");
        showSeat2.setIsAvailable(false);
        showSeatList.add(showSeat2);
        show.setShowSeatList(showSeatList);

        // Act & Assert
        assertThrows(RequestedSeatAreNotAvailable.class, () -> ticketService.ticketBooking(ticketEntryDto));
        verify(showRepository, times(1)).findById(ticketEntryDto.getShowId());
        verify(userRepository, times(1)).findById(ticketEntryDto.getUserId());
        verify(ticketRepository, never()).save(any());
        verify(userRepository, never()).save(any());
        verify(showRepository, never()).save(any());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

}