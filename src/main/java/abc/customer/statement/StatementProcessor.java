package abc.customer.statement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import abc.customer.statement.Statement.Transaction;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

/**
 * Of the 4 validations: reference uniqueness is implemented by this class
 * directly end balance is validated by @see
 * abc.customer.statement.StartPlusMutEqualsEnd start and end balances being >=
 * 0 are covered by a standard bean validation constraint
 * 
 * @see abc.customer.statement.Statement.Transaction future transaction dates
 *      are also covered by a standard bean validation constraint
 * @see abc.customer.statement.Statement.Transaction
 * 
 */
@Service
@RequiredArgsConstructor
public class StatementProcessor {
    private final Validator validator;

    public ValidationReport validate(Statement customerStatement) {
        final var lines = customerStatement.messages().stream()
                .collect(Collectors.groupingBy(Transaction::reference))
                .entrySet().stream()
                .flatMap(entry -> validateMessages(entry.getKey(),
                        entry.getValue()))
                .toList();
        return new ValidationReport(lines);
    }

    private Stream<ValidationReport.ValidationReportLine>
            validateMessages(long reference, List<Transaction> messages) {
        final List<String> uniqueness = (messages.size() > 1)
                ? List.of("Reference " + reference + " used multiple times")
                : List.of();

        return messages.stream()
                .map(message -> this.validateMessage(message, uniqueness))
                .flatMap(Optional::stream);
    }

    private Optional<ValidationReport.ValidationReportLine>
            validateMessage(Transaction message, List<String> uniqueness) {
        final var constraintviolations = validator.validate(message).stream()
                .map(ConstraintViolation::getMessage);
        final var errors = Stream
                .concat(constraintviolations, uniqueness.stream()).toList();

        return (errors.size() == 0) ? Optional.empty()
                : Optional.of(new ValidationReport.ValidationReportLine(
                        message.reference(), message.description(), errors));
    }
}
