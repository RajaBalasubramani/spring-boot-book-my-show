package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.TheaterEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.TheaterSeatEntryDto;
import com.driver.bookMyShow.Services.TheaterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TheaterControllerTest {

    @Mock
    private TheaterService theaterService;

    @InjectMocks
    private TheaterController theaterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTheater_ValidTheaterEntry_ReturnsCreatedResponse() {
        // Arrange
        TheaterEntryDto theaterEntryDto = new TheaterEntryDto();
        when(theaterService.addTheater(theaterEntryDto)).thenReturn("Theater added successfully");

        // Act
        ResponseEntity<String> response = theaterController.addTheater(theaterEntryDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Theater added successfully", response.getBody());
    }

    @Test
    void testAddTheater_InvalidTheaterEntry_ReturnsBadRequestResponse() {
        // Arrange
        TheaterEntryDto theaterEntryDto = new TheaterEntryDto();
        when(theaterService.addTheater(theaterEntryDto))
                .thenThrow(new IllegalArgumentException("Invalid theater entry"));

        // Act
        ResponseEntity<String> response = theaterController.addTheater(theaterEntryDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid theater entry", response.getBody());
    }

    @Test
    void testAddTheaterSeat_ValidTheaterSeatEntry_ReturnsCreatedResponse() {
        // Arrange
        TheaterSeatEntryDto theaterSeatEntryDto = new TheaterSeatEntryDto();
        when(theaterService.addTheaterSeat(theaterSeatEntryDto)).thenReturn("Theater seat added successfully");

        // Act
        ResponseEntity<String> response = theaterController.addTheaterSeat(theaterSeatEntryDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Theater seat added successfully", response.getBody());
    }

    @Test
    void testAddTheaterSeat_InvalidTheaterSeatEntry_ReturnsBadRequestResponse() {
        // Arrange
        TheaterSeatEntryDto theaterSeatEntryDto = new TheaterSeatEntryDto();
        when(theaterService.addTheaterSeat(theaterSeatEntryDto))
                .thenThrow(new IllegalArgumentException("Invalid theater seat entry"));

        // Act
        ResponseEntity<String> response = theaterController.addTheaterSeat(theaterSeatEntryDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid theater seat entry", response.getBody());
    }
}