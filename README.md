## Continuous Integration

This project uses **GitHub Actions** to automate the build and verification process.

### CI Pipeline

The CI workflow is triggered whenever code is pushed to the `main` branch or a Pull Request is created targeting `main`.

The pipeline performs the following steps:

1. Checks out the latest source code.
2. Sets up the Java 21 environment.
3. Starts a PostgreSQL 16 database service.
4. Configures the required application environment variables.
5. Executes the Maven build and verification process using `./mvnw clean verify`.

### Technologies Used

- Java 21
- Spring Boot
- Maven
- PostgreSQL 16
- GitHub Actions

The pipeline helps identify build and test failures early, ensuring that code changes are verified before they are merged into the main branch.
