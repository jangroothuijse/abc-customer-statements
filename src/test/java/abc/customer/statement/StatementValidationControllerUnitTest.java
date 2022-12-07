package abc.customer.statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@ExtendWith(MockitoExtension.class)
public class StatementValidationControllerUnitTest {
    @Mock
    private CsvStatementMapper csvMapper;
    @Mock
    private StatementProcessor statementProcessor;
    @Mock
    private XmlMapper xmlMapper;

    @InjectMocks
    private StatementValidationController statementValidationController;

    @Test
    void checkCsv(@Mock ValidationReport report, @Mock Statement statement)
            throws IOException {
        // Given
        final var csv = "csv";
        when(csvMapper.map(csv)).thenReturn(statement);
        when(statementProcessor.validate(statement)).thenReturn(report);

        // When
        final var result = statementValidationController.validateCsv(csv);

        // Then
        assertThat(result).isEqualTo(report);
    }

    @Test
    void checkXml(@Mock ValidationReport report, @Mock Statement statement)
            throws IOException {
        // Given
        final var xml = "xml";
        when(xmlMapper.readValue(xml, Statement.class)).thenReturn(statement);
        when(statementProcessor.validate(statement)).thenReturn(report);

        // When
        final var result = statementValidationController.validateXml(xml);

        // Then
        assertThat(result).isEqualTo(report);
    }

}
