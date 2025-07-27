# Restful-Booker API Test Automation

This repository contains an automated test suite for the [Restful-Booker API](https://restful-booker.herokuapp.com). It covers functional, integration, negative, and performance testing for key endpoints including `GET /booking`, `PATCH /booking/{id}`, and `DELETE /booking/{id}`.

---

## Overview

This project verifies the correctness, reliability, and performance of the Restful-Booker API using automated regression tests. It includes:
- End-to-end API testing
- Data-driven and negative test cases
- CI/CD integration using GitHub Actions
- Allure reporting for results and trends

---

## Technologies Used

- **Java** (Test Language)
- **JUnit** (Test Framework)
- **RestAssured** (HTTP Client for API Testing)
- **Maven** (Build Tool)
- **Allure** (Test Reporting)
- **GitHub Actions** (CI/CD)
- **SLF4J** (Logging)

---

## What’s Tested

### GET /booking
- Retrieve all booking IDs
- Filter by `firstname`, `lastname`, `checkin`, `checkout`
- Combined filters
- Invalid parameters
- Response structure and timing

### PATCH /booking/{id}
- Single and multiple field updates
- Nested `bookingdates` update
- Invalid ID, missing token, invalid data formats
- Idempotent behavior

### DELETE /booking/{id}
- Valid and invalid deletions
- Deletion verification (404 on GET)
- Token-based auth validation
- Concurrency tests

---

## Setup Instructions

### Prerequisites
- Java 11+
- Maven 3.6+
- Git

### Installation

```bash
git clone https://github.com/your-username/restful-booker-api-tests.git
cd restful-booker-api-tests
mvn install
```

---

## Running Tests

Run all tests locally:

```bash
mvn clean install
```

---

## Viewing Test Reports

Generate the Allure report:

```bash
mvn allure:report
```

Serve the Allure report locally:

```bash
allure serve target/allure-results
```
## Allure Test DashBoard

<img width="1917" height="878" alt="image" src="https://github.com/user-attachments/assets/6e083902-c755-4595-8a6e-936026fbe9ce" />
<img width="1083" height="898" alt="image" src="https://github.com/user-attachments/assets/037761ab-a2f0-48cf-938b-274599192d5f" />


