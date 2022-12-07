package abc.customer.statement;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import abc.customer.statement.Statement.Transaction;
import jakarta.validation.Validation;

class StatementProcessorTest {
    private final StatementProcessor statementProcessor =
            new StatementProcessor(
                    Validation.buildDefaultValidatorFactory().getValidator());

    @Test
    void testEmpty() {
        // When
        final var result =
                statementProcessor.validate(new Statement(List.of()));

        // Then
        assertThat(result.lines()).isEmpty();
    }

    private final Transaction good = new Transaction(0L, "iban", "desc",
            BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, LocalDate.MIN);

    @Test
    void testValid() {
        // When
        final var result =
                statementProcessor.validate(new Statement(List.of(good)));

        // Then
        assertThat(result.lines()).isEmpty();
    }

    @Test
    void testDuplicateReference() {
        // When
        final var result =
                statementProcessor.validate(new Statement(List.of(good, good)));

        // Then
        assertThat(result.lines()).hasSize(2);
        assertThat(result.lines().get(0).reference())
                .isEqualTo(good.reference());
        assertThat(result.lines().get(1).reference())
                .isEqualTo(good.reference());
    }

    private final Transaction future = new Transaction(1L, "iban", "desc",
            BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, LocalDate.MAX);

    @Test
    void testFuture() {
        // When
        final var result =
                statementProcessor.validate(new Statement(List.of(future)));

        // Then
        assertThat(result.lines()).hasSize(1);
        assertThat(result.lines().get(0).reference())
                .isEqualTo(future.reference());
    }
}
