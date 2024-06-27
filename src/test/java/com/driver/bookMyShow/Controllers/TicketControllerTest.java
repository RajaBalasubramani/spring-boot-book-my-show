package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.TicketEntryDto;
import com.driver.bookMyShow.Dtos.ResponseDtos.TicketResponseDto;
import com.driver.bookMyShow.Services.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTicketBooking_ValidTicketEntry_ReturnsCreatedResponse() {
        // Arrange
        TicketEntryDto ticketEntryDto = new TicketEntryDto();
        TicketResponseDto ticketResponseDto = new TicketResponseDto();
        when(ticketService.ticketBooking(ticketEntryDto)).thenReturn(ticketResponseDto);

        // Act
        ResponseEntity<TicketResponseDto> response = ticketController.ticketBooking(ticketEntryDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ticketResponseDto, response.getBody());
    }

    @Test
    void testTicketBooking_InvalidTicketEntry_ReturnsBadRequestResponse() {
        // Arrange
        TicketEntryDto ticketEntryDto = new TicketEntryDto();
        when(ticketService.ticketBooking(ticketEntryDto))
                .thenThrow(new IllegalArgumentException("Invalid ticket entry"));

        // Act
        ResponseEntity<TicketResponseDto> response = ticketController.ticketBooking(ticketEntryDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}