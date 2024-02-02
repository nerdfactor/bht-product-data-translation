# ProductDataTranslation

## Development server

Run `mvn spring-boot:run` for a dev server. The server provides a REST API for the frontend to consume at `http://localhost:8080/api`.

## Build

Run `mvn compile` to build the project. The build artifacts will be stored in the `target/` directory.

## Build Jar

Run `mvn package` to package the project. The jar will be stored in the `target/` directory.
Run `java -jar target/product-data-translation-0.1.0.jar` to run the jar.

## Running tests

Run `mvn test` to execute tests.

## Configuration

The application can be configured using the `application.yaml` file. See the default config at `src/main/resources` for
examples.
Possible configurations include:

```yaml
app:
  picture-config:
    picture-path: pictures  # Path to the pictures directory
    store-in: memory    # Where to store the pictures. Options are memory or file-storage.
  translator-config:
    factory: "StrategyTranslatorFactory"   # The factory to use for creating translators. Options are StrategyTranslatorFactory or BasicTranslatorFactory.
    translator: "StrategyTranslator"       # The translator to use. Options are StrategyTranslator or BasicTranslator.
    strategy-config:
      text-translation-strategy: "DeeplTranslationStrategy"               # The strategy to use for translating text. See translation-strategies for options.
      currency-conversion-strategy: "OpenExchangeConversionStrategy"          # The strategy to use for converting currency. See conversion-strategies for options.
```

If you want to use non default configuration (e.g. use PostgreSQL DB) please make sure to provide path to the `application.yaml` file using the `--spring.config.location` flag or copy the file to the same directory as the jar.


## Environment Setup

Run `docker-compose up` to start the database. The database will be available at `localhost:5432` with the
username `pdt` and password `paf2023`. The database name is `pdt`.
You can alter the database settings in `application.yaml` by changing the `datasource` section.

```yaml
spring:
  datasource:
    driverClassName: org.postgresql.Driver
    platform: postgres
    url: "jdbc:postgresql://localhost:5432/pdt"   # change local database
    username: pdt       # change local username
    password: paf2023   # change local password
```

Remove the `datasource` section to use the default in-memory database.
The database will be setup and seeded with data on application start.
