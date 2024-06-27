package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.ShowEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowTimingsDto;
import com.driver.bookMyShow.Services.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShowControllerTest {

    @Mock
    private ShowService showService;

    @InjectMocks
    private ShowController showController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddShow_ValidShowEntry_ReturnsCreatedResponse() {
        // Arrange
        ShowEntryDto showEntryDto = new ShowEntryDto();
        when(showService.addShow(showEntryDto)).thenReturn("Show added successfully");

        // Act
        ResponseEntity<String> response = showController.addShow(showEntryDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Show added successfully", response.getBody());
    }

    @Test
    void testAddShow_InvalidShowEntry_ReturnsBadRequestResponse() {
        // Arrange
        ShowEntryDto showEntryDto = new ShowEntryDto();
        when(showService.addShow(showEntryDto)).thenThrow(new IllegalArgumentException("Invalid show entry"));

        // Act
        ResponseEntity<String> response = showController.addShow(showEntryDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid show entry", response.getBody());
    }

    @Test
    void testAssociateShowSeats_ValidShowSeatEntry_ReturnsCreatedResponse() {
        // Arrange
        ShowSeatEntryDto showSeatEntryDto = new ShowSeatEntryDto();
        when(showService.associateShowSeats(showSeatEntryDto)).thenReturn("Show seats associated successfully");

        // Act
        ResponseEntity<String> response = showController.associateShowSeats(showSeatEntryDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Show seats associated successfully", response.getBody());
    }

    @Test
    void testAssociateShowSeats_InvalidShowSeatEntry_ReturnsBadRequestResponse() {
        // Arrange
        ShowSeatEntryDto showSeatEntryDto = new ShowSeatEntryDto();
        when(showService.associateShowSeats(showSeatEntryDto)).thenThrow(new IllegalArgumentException("Invalid show seat entry"));

        // Act
        ResponseEntity<String> response = showController.associateShowSeats(showSeatEntryDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid show seat entry", response.getBody());
    }

    @Test
    void testShowTimingsOnDate_ValidShowTimingsDto_ReturnsFoundResponse() {
        // Arrange
        ShowTimingsDto showTimingsDto = new ShowTimingsDto();
        List<Time> showTimings = List.of(Time.valueOf("10:00:00"), Time.valueOf("14:00:00"), Time.valueOf("18:00:00"));
        when(showService.showTimingsOnDate(showTimingsDto)).thenReturn(showTimings);

        // Act
        ResponseEntity<List<Time>> response = showController.showTimingsOnDate(showTimingsDto);

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(showTimings, response.getBody());
    }

    @Test
    void testShowTimingsOnDate_InvalidShowTimingsDto_ReturnsNotFoundResponse() {
        // Arrange
        ShowTimingsDto showTimingsDto = new ShowTimingsDto();
        when(showService.showTimingsOnDate(showTimingsDto)).thenThrow(new IllegalArgumentException("Invalid show timings"));

        // Act
        ResponseEntity<List<Time>> response = showController.showTimingsOnDate(showTimingsDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testMovieHavingMostShows_ReturnsFoundResponse() {
        // Arrange
        String movie = "Avengers: Endgame";
        when(showService.movieHavingMostShows()).thenReturn(movie);

        // Act
        ResponseEntity<String> response = showController.movieHavingMostShows();

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(movie, response.getBody());
    }

    @Test
    void testMovieHavingMostShows_ReturnsNotFoundResponse() {
        // Arrange
        when(showService.movieHavingMostShows()).thenThrow(new IllegalArgumentException("No movies found"));

        // Act
        ResponseEntity<String> response = showController.movieHavingMostShows();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}