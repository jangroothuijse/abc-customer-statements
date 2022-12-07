package abc.customer.statement;

import java.io.IOException;

public interface XmlStatementMapper {
    Statement map(String csv) throws IOException;
}
