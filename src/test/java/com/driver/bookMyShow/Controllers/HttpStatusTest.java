package com.driver.bookMyShow.Controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpStatusTest {

    @Test
    void testHttpStatus_OK() {
        // Arrange
        HttpStatus status = HttpStatus.OK;

        // Act
        int statusCode = status.value();
        String reasonPhrase = status.getReasonPhrase();

        // Assert
        assertEquals(200, statusCode);
        assertEquals("OK", reasonPhrase);
    }

    @Test
    void testHttpStatus_CREATED() {
        // Arrange
        HttpStatus status = HttpStatus.CREATED;

        // Act
        int statusCode = status.value();
        String reasonPhrase = status.getReasonPhrase();

        // Assert
        assertEquals(201, statusCode);
        assertEquals("Created", reasonPhrase);
    }

    @Test
    void testHttpStatus_BAD_REQUEST() {
        // Arrange
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Act
        int statusCode = status.value();
        String reasonPhrase = status.getReasonPhrase();

        // Assert
        assertEquals(400, statusCode);
        assertEquals("Bad Request", reasonPhrase);
    }

    // Add more test cases for other HttpStatus values if needed

}