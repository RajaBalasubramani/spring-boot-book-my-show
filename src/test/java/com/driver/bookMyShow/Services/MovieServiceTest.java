package com.driver.bookMyShow.Services;
import com.driver.bookMyShow.Dtos.RequestDtos.MovieEntryDto;
import com.driver.bookMyShow.Enums.Language;
import com.driver.bookMyShow.Exceptions.MovieAlreadyPresentWithSameNameAndLanguage;
import com.driver.bookMyShow.Models.Movie;
import com.driver.bookMyShow.Repositories.MovieRepository;
import com.driver.bookMyShow.Transformers.MovieTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMovie_WhenMovieDoesNotExist_ShouldAddMovieAndReturnSuccessMessage() throws MovieAlreadyPresentWithSameNameAndLanguage {
        // Arrange
        MovieEntryDto movieEntryDto = new MovieEntryDto();
        movieEntryDto.setMovieName("Avengers: Endgame");
        movieEntryDto.setLanguage(Language.ENGLISH);

        Movie movie = MovieTransformer.movieDtoToMovie(movieEntryDto);

        when(movieRepository.findByMovieName(movieEntryDto.getMovieName())).thenReturn(null);
        when(movieRepository.save(movie)).thenReturn(movie);

        // Act
        String result = movieService.addMovie(movieEntryDto);

        // Assert
        assertEquals("The movie has been added successfully", result);
        verify(movieRepository, times(1)).findByMovieName(movieEntryDto.getMovieName());
        verify(movieRepository, times(1)).save(movie);
    }

    // Add more test cases for different scenarios

}