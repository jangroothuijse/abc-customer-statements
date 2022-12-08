# abc-customer-statements

## How to run

1. Start the application
`mvn spring-boot:run`

2. Then, in another terminal, check xml
`sh test_xml.sh`

3. And finaly check csv
`sh test_csv.sh`

## What it does

The application listens on port 8080, and has 2 post endpoints on `/validate`, one for Content-Type `application/xml` and one for `txt/csv`.  Please see the sh files `test_xml.sh` and `test_csv.sh` for example usage.

## More docs

Please see `abc.customer.statement.Application`, it is a starting point for javadoc.
