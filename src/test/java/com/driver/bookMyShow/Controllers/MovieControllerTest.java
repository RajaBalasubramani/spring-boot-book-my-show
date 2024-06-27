package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Dtos.RequestDtos.MovieEntryDto;
import com.driver.bookMyShow.Services.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMovie_ValidMovieEntry_ReturnsCreatedResponse() {
        // Arrange
        MovieEntryDto movieEntryDto = new MovieEntryDto();
        when(movieService.addMovie(movieEntryDto)).thenReturn("Movie added successfully");

        // Act
        ResponseEntity<String> response = movieController.addMovie(movieEntryDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Movie added successfully", response.getBody());
    }

    @Test
    void testAddMovie_InvalidMovieEntry_ReturnsBadRequestResponse() {
        // Arrange
        MovieEntryDto movieEntryDto = new MovieEntryDto();
        when(movieService.addMovie(movieEntryDto)).thenThrow(new IllegalArgumentException("Invalid movie entry"));

        // Act
        ResponseEntity<String> response = movieController.addMovie(movieEntryDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid movie entry", response.getBody());
    }

    @Test
    void testTotalCollection_ValidMovieId_ReturnsCollectionAmount() {
        // Arrange
        int movieId = 1;
        when(movieService.totalCollection(movieId)).thenReturn(100000L);

        // Act
        ResponseEntity<Long> response = movieController.totalCollection(movieId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(100000L, response.getBody());
    }

    @Test
    void testTotalCollection_InvalidMovieId_ReturnsBadRequestResponse() {
        // Arrange
        int movieId = 1;
        when(movieService.totalCollection(movieId)).thenThrow(new IllegalArgumentException("Invalid movie ID"));

        // Act
        ResponseEntity<Long> response = movieController.totalCollection(movieId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}