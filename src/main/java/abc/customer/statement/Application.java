package abc.customer.statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import abc.customer.statement.Statement.Transaction;

/**
 * Application which offers 2 rest endpoints which validate a customer
 * statement.
 * 
 * For validation @see abc.customer.statement.StatementProcessor For conversions
 * from xml and csv @see abc.customer.statement.Statement For the api
 * layer, @see abc.customer.statement.StatementValidationController
 */
@SpringBootApplication
public class Application {

    @Bean
    public ObjectMapper objectMapper() {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public XmlMapper xmlMapper() {
        final var xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.registerModule(new ParameterNamesModule());
        return xmlMapper;
    }

    @Bean
    public CsvStatementMapper cSVCustomerStatementMapper() {
        final var csvMapper = CsvMapper.builder()
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).build();
        csvMapper.registerModule(new JavaTimeModule());
        final var csvSchema = csvMapper.schemaFor(Transaction.class)
                .withHeader().withColumnSeparator(',');
        final var objectReader =
                csvMapper.readerFor(Transaction.class).with(csvSchema);
        return csv -> new Statement(
                objectReader.<Transaction>readValues(csv).readAll());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
