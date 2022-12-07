package abc.customer.statement;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import abc.customer.statement.Statement.Transaction;

class ApplicationUnitTest {
    @Test
    void testCsvParsing() throws IOException {
        // Given
        final var csv =
                """
                        				Reference,Account Number,Description,Start Balance,Mutation,End Balance, Transaction Date
                        177666,NL93ABNA0585619023,Flowers for Rik Theu�,44.85,-22.24,22.61,2015-08-12
                        112806,NL69ABNA0433647324,Subscription for Jan Theu�,45.59,+48.18,93.77,2014-04-15
                        158338,NL91RABO0315273637,Tickets for Vincent King,12.76,-39.5,-26.74,2009-10-21
                        193499,NL93ABNA0585619023,Candy for Dani�l Dekker,88.44,-13.28,75.16,2013-08-12
                        112806,NL90ABNA0585647886,Clothes from Peter de Vries,32.76,+49.03,81.79,2012-04-21
                        112806,NL91RABO0315273637,Tickets for Erik Dekker,41.63,+12.41,54.04,2013-02-06
                        108230,NL32RABO0195610843,Flowers for Willem Bakker,43.63,-12.18,31.45,2013-10-05
                        196213,NL32RABO0195610843,Subscription from Rik de Vries,30.36,-35.1,-4.74,2011-08-12
                        109762,NL93ABNA0585619023,Flowers from Rik de Vries,47.45,+17.82,65.27,2001-08-12
                        163590,NL27SNSB0917829871,Tickets from Rik Bakker,105.11,+29.87,134.98,2004-07-21
                        				""";
        // When
        final var statement =
                new Application().cSVCustomerStatementMapper().map(csv);
        
        // Then
        assertThat(statement.messages()).hasSize(10);
        final var expected = new Transaction(
                177666L, "NL93ABNA0585619023", "Flowers for Rik Theu�",
                new BigDecimal("44.85"), new BigDecimal("-22.24"),
                new BigDecimal("22.61"), LocalDate.of(2015, 8, 12));
        assertThat(statement.messages().get(0)).isEqualTo(expected);
    }

    @Test
    void textXmlParsing() throws JsonMappingException, JsonProcessingException {
        // Given
        final var xml = """
                <records>
                  <record reference="187997">
                    <accountNumber>NL91RABO0315273637</accountNumber>
                    <description>Clothes for Rik King</description>
                    <startBalance>57.6</startBalance>
                    <mutation>-32.98</mutation>
                    <endBalance>24.62</endBalance>
                    <date>2019-01-09</date>
                  </record>
                  <record reference="154270">
                    <accountNumber>NL56RABO0149876948</accountNumber>
                    <description>Candy for Peter de Vries</description>
                    <startBalance>5429</startBalance>
                    <mutation>-939</mutation>
                    <endBalance>6368</endBalance>
                    <date>2016-12-09</date>
                  </record>
                  <record reference="162197">
                    <accountNumber>NL90ABNA0585647886</accountNumber>
                    <description>Tickets for Daniël de Vries</description>
                    <startBalance>95.03</startBalance>
                    <mutation>+48.33</mutation>
                    <endBalance>143.36</endBalance>
                    <date>2013-12-06</date>
                  </record>
                  <record reference="129635">
                    <accountNumber>NL27SNSB0917829871</accountNumber>
                    <description>Clothes for Vincent King</description>
                    <startBalance>14.48</startBalance>
                    <mutation>+16.39</mutation>
                    <endBalance>30.87</endBalance>
                    <date>2016-04-19</date>
                  </record>
                  <record reference="148503">
                    <accountNumber>NL93ABNA0585619023</accountNumber>
                    <description>Subscription from Willem Dekker</description>
                    <startBalance>30.54</startBalance>
                    <mutation>-13.18</mutation>
                    <endBalance>17.36</endBalance>
                    <date>2016-12-09</date>
                  </record>
                  <record reference="163023">
                    <accountNumber>NL43AEGO0773393871</accountNumber>
                    <description>Tickets for Daniël de Vries</description>
                    <startBalance>37.79</startBalance>
                    <mutation>-40.84</mutation>
                    <endBalance>-3.05</endBalance>
                    <date>2016-12-09</date>
                  </record>
                  <record reference="162410">
                    <accountNumber>NL69ABNA0433647324</accountNumber>
                    <description>Tickets from Jan Bakker</description>
                    <startBalance>10.1</startBalance>
                    <mutation>-0.3</mutation>
                    <endBalance>9.8</endBalance>
                    <date>2016-12-09</date>
                  </record>
                  <record reference="112747">
                    <accountNumber>NL56RABO0149876948</accountNumber>
                    <description>Candy from Jan Dekker</description>
                    <startBalance>51.62</startBalance>
                    <mutation>-42.36</mutation>
                    <endBalance>9.26</endBalance>
                    <date>2016-08-09</date>
                  </record>
                  <record reference="140269">
                    <accountNumber>NL43AEGO0773393871</accountNumber>
                    <description>Tickets for Vincent Dekker</description>
                    <startBalance>3980</startBalance>
                    <mutation>+1000</mutation>
                    <endBalance>4981</endBalance>
                    <date>2012-11-29</date>
                  </record>
                  <record reference="115137">
                    <accountNumber>NL43AEGO0773393871</accountNumber>
                    <description>Flowers for Jan Theuß</description>
                    <startBalance>28.19</startBalance>
                    <mutation>+3.22</mutation>
                    <endBalance>31.41</endBalance>
                    <date>2013-08-12</date>
                  </record>
                </records>
                """;
        final var xmlMapper = new Application().xmlMapper();

        // When
        final var statement = xmlMapper.readValue(xml, Statement.class);

        // Then
        assertThat(statement.messages()).hasSize(10);
        final var expected = new Transaction(187997L, "NL91RABO0315273637",
                "Clothes for Rik King", new BigDecimal("57.6"),
                new BigDecimal("-32.98"), new BigDecimal("24.62"),
                LocalDate.of(2019, 1, 9));
        assertThat(statement.messages().get(0)).isEqualTo(expected);
    }
}
