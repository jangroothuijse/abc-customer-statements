package abc.customer.statement;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.RequiredArgsConstructor;

@RequestMapping("/validate")
@RestController
@RequiredArgsConstructor
public class StatementValidationController {
    private final CsvStatementMapper csvMapper;
    private final StatementProcessor statementProcessor;
    private final XmlMapper xmlMapper;

    @PostMapping(consumes = "text/csv")
    public ValidationReport validateCsv(@RequestBody String csv)
            throws IOException {
        final Statement statement = csvMapper.map(csv);
        return statementProcessor.validate(statement);
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ValidationReport validateXml(@RequestBody String xml)
            throws IOException {
        final Statement statement = xmlMapper.readValue(xml, Statement.class);
        return statementProcessor.validate(statement);
    }
}
