package abc.customer.statement;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class StartPlusMutEqualsEndTest {
    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidationError() {
        // Given
        final var doesNotAddUp =
                new Statement.Transaction(0L, "", "", BigDecimal.ONE,
                        BigDecimal.ONE, BigDecimal.ONE, LocalDate.now());

        // When
        final var errors = validator.validate(doesNotAddUp);
        
        // Then
        final var error = errors.stream().findFirst();
        assertThat(error).isPresent();
        assertThat(error.get().getMessage())
                .isEqualTo(StartPlusMutEqualsEnd.MESSAGE);
    }

    @Test
    void testValidationPass() {
        // Given
        final var doesNotAddUp =
                new Statement.Transaction(0L, "", "", BigDecimal.ONE,
                        BigDecimal.ZERO, BigDecimal.ONE, LocalDate.now());

        // When
        final var errors = validator.validate(doesNotAddUp);
        
        // Then
        assertThat(errors).isEmpty();
    }
}
