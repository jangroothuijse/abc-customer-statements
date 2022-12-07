package abc.customer.statement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Normally I do not recommend using one model to convert to both xml and csv.
 */
@JacksonXmlRootElement(localName = "records")
public record Statement(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "record")
        List<Transaction> messages) {

    @StartPlusMutEqualsEnd
    public record Transaction(
            @JacksonXmlProperty(isAttribute = true, localName = "reference")
            long reference,
            String accountNumber, String description,
            @PositiveOrZero(message = "startBalance must be greater than or equal to 0")
            BigDecimal startBalance,
            BigDecimal mutation,
            @PositiveOrZero(message = "endBalance must be greater than or equal to 0")
            BigDecimal endBalance,
            @PastOrPresent LocalDate date) {
    }
}
