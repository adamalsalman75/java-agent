# Gemini Guidelines for the java-agent Project

## 1. Project Goal

Simple agentic workflow. Two micro agents. A writer and a reviewer. The writer writes a story. The reviewer
reviews. They iterate until happy.

## 2. Core Rules & Collaboration

- **Confirm Plans:** Always propose a plan and get my approval before making any code changes.
- **Architecture:** Do not change the core architecture or add new major dependencies without discussing it first.
- **Dependencies:** Do not change dependency versions in `pom.xml` without my approval.

## 3. Development Workflow

- **Build:** To build the project, run: `mvnw clean install`
- **Run:** To run the application, use: `java -jar target/java-agent-0.0.1-SNAPSHOT.jar`
- **Test:** To run all tests, use: `mvnw test`

## 4. Technology & Code Conventions

- **Immutability:** Prefer immutable Java Records for Data Transfer Objects (DTOs) and other value objects.
- **HTTP Clients:** Use `RestClient` for new HTTP client implementations. Avoid the legacy `RestTemplate`.
- **Configuration:** All configuration should be in `src/main/resources/application.yaml`.
- **Asynchronous Programming:** For asynchronous operations, use Java's virtual threads and the structured task scope API. Avoid reactive programming models.
- **Secrets:** Secrets must be externalized. For local development, they will be provided in a `.env` file which is git-ignored. Never commit secrets to the repository.
