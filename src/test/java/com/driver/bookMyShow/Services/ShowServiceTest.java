package com.driver.bookMyShow.Services;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowSeatEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.ShowTimingsDto;
import com.driver.bookMyShow.Enums.SeatType;
import com.driver.bookMyShow.Exceptions.MovieDoesNotExists;
import com.driver.bookMyShow.Exceptions.ShowDoesNotExists;
import com.driver.bookMyShow.Exceptions.TheaterDoesNotExists;
import com.driver.bookMyShow.Models.Movie;
import com.driver.bookMyShow.Models.Show;
import com.driver.bookMyShow.Models.Theater;
import com.driver.bookMyShow.Models.TheaterSeat;
import com.driver.bookMyShow.Repositories.MovieRepository;
import com.driver.bookMyShow.Repositories.ShowRepository;
import com.driver.bookMyShow.Repositories.TheaterRepository;
import com.driver.bookMyShow.Transformers.ShowTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ShowServiceTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheaterRepository theaterRepository;

    @InjectMocks
    private ShowService showService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addShow_shouldAddShowSuccessfully() throws MovieDoesNotExists, TheaterDoesNotExists {
        // Arrange
        ShowEntryDto showEntryDto = new ShowEntryDto();
        showEntryDto.setMovieId(1);
        showEntryDto.setTheaterId(1);

        Show show = new Show();
        show.setShowId(1);
        show.setMovie(new Movie());
        show.setTheater(new Theater());

        Movie movie = new Movie();
        movie.setId(1);
        movie.getShows().add(show);

        Theater theater = new Theater();
        theater.setId(1);
        theater.getShowList().add(show);

        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(theaterRepository.findById(1)).thenReturn(Optional.of(theater));
        when(showRepository.save(any(Show.class))).thenReturn(show);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(theaterRepository.save(any(Theater.class))).thenReturn(theater);

        // Act
        String result = showService.addShow(showEntryDto);

        // Assert
        assertEquals("Show has been added Successfully", result);
        verify(showRepository, times(1)).save(any(Show.class));
        verify(movieRepository, times(1)).save(any(Movie.class));
        verify(theaterRepository, times(1)).save(any(Theater.class));
    }

    @Test
    void addShow_shouldThrowMovieDoesNotExistsException() {
        // Arrange
        ShowEntryDto showEntryDto = new ShowEntryDto();
        showEntryDto.setMovieId(1);
        showEntryDto.setTheaterId(1);

        when(movieRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MovieDoesNotExists.class, () -> showService.addShow(showEntryDto));
        verify(showRepository, never()).save(any(Show.class));
        verify(movieRepository, never()).save(any(Movie.class));
        verify(theaterRepository, never()).save(any(Theater.class));
    }

    @Test
    void addShow_shouldThrowTheaterDoesNotExistsException() {
        // Arrange
        ShowEntryDto showEntryDto = new ShowEntryDto();
        showEntryDto.setMovieId(1);
        showEntryDto.setTheaterId(1);

        when(movieRepository.findById(1)).thenReturn(Optional.of(new Movie()));
        when(theaterRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TheaterDoesNotExists.class, () -> showService.addShow(showEntryDto));
        verify(showRepository, never()).save(any(Show.class));
        verify(movieRepository, never()).save(any(Movie.class));
        verify(theaterRepository, never()).save(any(Theater.class));
    }


    @Test
    void associateShowSeats_shouldThrowShowDoesNotExistsException() {
        // Arrange
        ShowSeatEntryDto showSeatEntryDto = new ShowSeatEntryDto();
        showSeatEntryDto.setShowId(1);

        when(showRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ShowDoesNotExists.class, () -> showService.associateShowSeats(showSeatEntryDto));
        verify(showRepository, never()).save(any(Show.class));
    }

    @Test
    void showTimingsOnDate_shouldReturnShowTimings() {
        // Arrange
        ShowTimingsDto showTimingsDto = new ShowTimingsDto();
        showTimingsDto.setDate(new Date(2022, 1, 1));
        showTimingsDto.setTheaterId(1);
        showTimingsDto.setMovieId(1);

        List<Time> showTimings = new ArrayList<>();
        showTimings.add(new Time(10, 0, 0));
        showTimings.add(new Time(12, 0, 0));

        when(showRepository.getShowTimingsOnDate(new Date(2022, 1, 1), 1, 1)).thenReturn(showTimings);

        // Act
        List<Time> result = showService.showTimingsOnDate(showTimingsDto);

        // Assert
        assertEquals(showTimings, result);
    }

}