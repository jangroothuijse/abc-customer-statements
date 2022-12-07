package abc.customer.statement;

import java.io.IOException;

/**
 * The actual CsvMapper can only map a specific type. However, its method
 * signatures would tell you otherwise. That is what this wrapper tries to
 * solves.
 */
public interface CsvStatementMapper {
    Statement map(String csv) throws IOException;
}
