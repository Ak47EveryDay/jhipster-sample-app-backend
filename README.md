# jhipster-sample-app-backend

Spring Boot backend of the JHipster sample application (bank accounts, labels and operations), split out of [jhipster/jhipster-sample-app](https://github.com/jhipster/jhipster-sample-app) (generated with JHipster 9.4.0, commit `6b000b5d`).

It is an API server only: it no longer builds, packages or serves the Angular application. The Angular application lives in a separate repository, [jhipster-sample-app-frontend](https://github.com/Ak47EveryDay/jhipster-sample-app-frontend).

## Documentation (Japanese)

Design documents and development guidelines, written in Japanese and based on this repository's code, are in [docs/](docs/README.md):

- [docs/050_内部設計](docs/050_内部設計/README.md): internal design — architecture, common specifications, per-function processing and REST interfaces, tables, repositories, database migrations
- [docs/999_標準化](docs/999_標準化/README.md): standards and system knowledge — layering, common components, coding conventions, development procedures, tests, configuration and build

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
Only the API paths are served. Any other path is rejected: `401` when the request is not signed in, `403` when it is. `ApiOnlyIT` pins this.

The frontend dev server proxies `/api`, `/management`, `/v3/api-docs` and `/h2-console` to this backend. If the frontend is served from another origin instead, add that origin to `jhipster.cors.allowed-origins` in `src/main/resources/config/application-dev.yml` (it is switched off by default in `application-prod.yml`).

Account activation and password reset emails link to pages of the frontend, so `jhipster.mail.base-url` has to be the frontend's address. It is `http://localhost:9060` in `application-dev.yml`; in production set it in `application-prod.yml` or override it with an environment variable.

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

Running with the `prod` profile needs two things that the `dev` profile gets for free:

- A PostgreSQL database. The connection settings are in `src/main/resources/config/application-prod.yml` and can be overridden with the `SPRING_DATASOURCE_*` environment variables.
- A JWT secret, because `prod` does not load the sample secrets: set `JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET` to the base64 encoding of at least 64 random bytes. For a local trial only, `--spring.profiles.active=prod,secret-samples` uses a well-known sample value instead.

The remaining configuration is in `src/main/resources/config/application-prod.yml` and can be overridden with environment variables.

A Docker image can be built with `./mvnw -ntp verify -DskipTests -Pprod jib:dockerBuild`. Docker Compose files for PostgreSQL, monitoring, Sonar and JHipster Control Center are in `src/main/docker`.

## What changed in the split

- `pom.xml` no longer installs Node or builds the frontend. The `webapp` and `war` profiles, the frontend Maven plugin and the checksum plugin were removed.
- The classes that served the Angular application were removed: `SpaWebFilter` (forwarded unmapped paths to `index.html`), `StaticResourcesWebConfiguration` (cache headers for static files) and the document-root customization in `WebConfigurer`. Static file paths are no longer publicly allowed in `SecurityConfiguration`. `ApiOnlyIT` pins this behavior.
- `.yo-rc.json` and `.jhipster/` were removed: the project can no longer be regenerated with JHipster. Entities are defined by the code in `domain/`.

## License

Apache-2.0, see `LICENSE.txt`.
