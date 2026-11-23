# jhipster-sample-app-backend

Spring Boot backend of the JHipster sample application (bank accounts, labels and operations), split out of [jhipster/jhipster-sample-app](https://github.com/jhipster/jhipster-sample-app) (generated with JHipster 9.4.0, commit `6b000b5d`).

It is an API server only: it no longer builds, packages or serves the Angular application. The Angular application lives in a separate repository, `jhipster-sample-app-frontend`.

## Requirements

- Java 21
- Docker, only to run the `prod` profile tests (they start PostgreSQL through Testcontainers)

## Development

Start the backend on port 8080 with an in-memory H2 database:

```bash
./mvnw
```

Then check `http://localhost:8080/management/health`. The H2 console is available at `http://localhost:8080/h2-console`.

There is no user interface on port 8080 any more: start the frontend repository and open `http://localhost:9060`.

The frontend dev server proxies `/api`, `/management`, `/v3/api-docs` and `/h2-console` to this backend. If the frontend is served from another origin instead, add that origin to `jhipster.cors.allowed-origins` in `src/main/resources/config/application-dev.yml` (it is switched off by default in `application-prod.yml`).

## Building and testing

```bash
# unit and integration tests against H2, then package target/*.jar
./mvnw verify

# integration tests against PostgreSQL in a Docker container, then package
./mvnw -Pprod verify
```

## Production

```bash
./mvnw -Pprod clean verify
java -jar target/*.jar
```

Configuration for production is in `src/main/resources/config/application-prod.yml` and can be overridden with environment variables.

A Docker image can be built with `./mvnw -ntp verify -DskipTests -Pprod jib:dockerBuild`. Docker Compose files for PostgreSQL, monitoring, Sonar and JHipster Control Center are in `src/main/docker`.

## What changed in the split

- `pom.xml` no longer installs Node or builds the frontend. The `webapp` and `war` profiles, the frontend Maven plugin and the checksum plugin were removed.
- The classes that served the Angular application were removed: `SpaWebFilter` (forwarded unmapped paths to `index.html`), `StaticResourcesWebConfiguration` (cache headers for static files) and the document-root customization in `WebConfigurer`. Static file paths are no longer publicly allowed in `SecurityConfiguration`. `ApiOnlyIT` pins this behavior.
- `.yo-rc.json` and `.jhipster/` were removed: the project can no longer be regenerated with JHipster. Entities are defined by the code in `domain/`.

## License

Apache-2.0, see `LICENSE.txt`.
