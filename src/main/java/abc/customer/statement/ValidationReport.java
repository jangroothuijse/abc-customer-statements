package abc.customer.statement;

import java.util.List;

public record ValidationReport(List<ValidationReportLine> lines) {
    public record ValidationReportLine(long reference, String description,
            List<String> messages) {
    }
}
