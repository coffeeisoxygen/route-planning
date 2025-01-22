package com.coffeecode.domain.validation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CoordinateValidatorTest {
    
    @ParameterizedTest
    @CsvSource({
        "-91.0, 0.0",
        "91.0, 0.0",
        "0.0, -181.0",
        "0.0, 181.0"
    })
    void validate_withInvalidCoordinates_shouldThrowException(
            double lat, double lon) {
        assertThrows(ValidationException.class, 
            () -> CoordinateValidator.validate(lat, lon));
    }

    @Test
    void validate_withValidCoordinates_shouldNotThrow() {
        assertDoesNotThrow(() -> 
            CoordinateValidator.validate(-6.914744, 107.609810));
    }
}