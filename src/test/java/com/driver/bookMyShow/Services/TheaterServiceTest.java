package com.driver.bookMyShow.Services;

import com.driver.bookMyShow.Dtos.RequestDtos.TheaterEntryDto;
import com.driver.bookMyShow.Dtos.RequestDtos.TheaterSeatEntryDto;
import com.driver.bookMyShow.Enums.SeatType;
import com.driver.bookMyShow.Exceptions.TheaterIsNotPresentOnThisAddress;
import com.driver.bookMyShow.Exceptions.TheaterIsPresentOnThatAddress;
import com.driver.bookMyShow.Models.Theater;
import com.driver.bookMyShow.Models.TheaterSeat;
import com.driver.bookMyShow.Repositories.TheaterRepository;
import com.driver.bookMyShow.Transformers.TheaterTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TheaterServiceTest {

    @Mock
    private TheaterRepository theaterRepository;

    @InjectMocks
    private TheaterService theaterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTheater_WhenTheaterIsPresentOnThatAddress_ShouldThrowTheaterIsPresentOnThatAddress() {
        // Arrange
        TheaterEntryDto theaterEntryDto = new TheaterEntryDto();
        theaterEntryDto.setAddress("123 Main St");

        when(theaterRepository.findByAddress(theaterEntryDto.getAddress())).thenReturn(new Theater());

        // Act & Assert
        assertThrows(TheaterIsPresentOnThatAddress.class, () -> theaterService.addTheater(theaterEntryDto));
        verify(theaterRepository, times(1)).findByAddress(theaterEntryDto.getAddress());
        verify(theaterRepository, never()).save(any(Theater.class));
    }

    @Test
    void addTheater_WhenTheaterIsNotPresentOnThatAddress_ShouldSaveTheater() throws TheaterIsPresentOnThatAddress {
        // Arrange
        TheaterEntryDto theaterEntryDto = new TheaterEntryDto();
        theaterEntryDto.setAddress("123 Main St");

        // when(theaterRepository.findByAddress(theaterEntryDto.getAddress())).thenReturn(null);

        // Act
        String result = theaterService.addTheater(theaterEntryDto);

        // Assert
        verify(theaterRepository, times(1)).findByAddress(theaterEntryDto.getAddress());
        verify(theaterRepository, times(1)).save(any(Theater.class));
        assertEquals("Theater has been saved Successfully", result);
    }

    @Test
    void addTheaterSeat_WhenTheaterIsNotPresentOnThisAddress_ShouldThrowTheaterIsNotPresentOnThisAddress() {
        // Arrange
        TheaterSeatEntryDto entryDto = new TheaterSeatEntryDto();
        entryDto.setAddress("123 Main St");

        when(theaterRepository.findByAddress(entryDto.getAddress())).thenReturn(null);

        // Act & Assert
        assertThrows(TheaterIsNotPresentOnThisAddress.class, () -> theaterService.addTheaterSeat(entryDto));
        verify(theaterRepository, times(1)).findByAddress(entryDto.getAddress());
        verify(theaterRepository, never()).save(any(Theater.class));
    }

    @Test
    void addTheaterSeat_WhenTheaterIsPresentOnThisAddress_ShouldAddTheaterSeats()
            throws TheaterIsNotPresentOnThisAddress {
        // Arrange
        TheaterSeatEntryDto entryDto = new TheaterSeatEntryDto();
        entryDto.setAddress("123 Main St");
        entryDto.setNoOfSeatInRow(5);
        entryDto.setNoOfPremiumSeat(10);
        entryDto.setNoOfClassicSeat(20);

        Theater theater = new Theater();
        when(theaterRepository.findByAddress(entryDto.getAddress())).thenReturn(theater);

        // Act
        String result = theaterService.addTheaterSeat(entryDto);

        // Assert
        assertEquals("Theater Seats have been added successfully", result);
    }
}