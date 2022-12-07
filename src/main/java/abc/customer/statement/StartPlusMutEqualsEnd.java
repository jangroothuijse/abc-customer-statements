package abc.customer.statement;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import abc.customer.statement.Statement.Transaction;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;

/**
 * One of the validations executed on statements.
 */
@Retention(RUNTIME)
@Target({ TYPE })
@Documented
@Constraint(validatedBy = { StartPlusMutEqualsEnd.Validator.class })
public @interface StartPlusMutEqualsEnd {
    String MESSAGE = """
            startBalance and mutation do not add up to endBalance""";

    String message() default MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements
            ConstraintValidator<StartPlusMutEqualsEnd, Statement.Transaction> {

        @Override
        public boolean isValid(
                final Transaction value,
                final ConstraintValidatorContext context) {
            final var expected = value.startBalance().add(value.mutation());
            return value.endBalance().compareTo(expected) == 0;
        }
    }
}
