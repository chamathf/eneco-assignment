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

## Project Structure

```
EnecoAssignment/
├── src/
│   └── test/
│       └── java/
│           └── api/
│               ├── booking/
│               │   ├── DeleteBookingTests.java
│               │   ├── GetBookingTests.java
│               │   ├── UpdateBookingTests.java
│               ├── integration/
│               │   └── BookingE2ETests.java
│               ├── negative/
│               │   └── NegativeBookingTests.java
│               ├── base/
│               │   └── BaseApiTest.java
│               └── utils/
│                   ├── ApiClient.java
│                   ├── AuthenticationHelper.java
│                   └── TestDataLoader.java
│
│       └── resources/
│           └── testdata/
│               ├── authCredentials.json
│               ├── bulkCreationBooking.json
│               ├── createBooking.json
│               ├── deleteBooking.json
│               ├── getBooking.json
│               ├── negativeBooking.json
│               └── updateBooking.json
│
├── target/
├── allure-results/
└── README.md
```

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

<img width="1907" height="885" alt="image" src="https://github.com/user-attachments/assets/9c6cd057-afa6-45f1-a882-b4eb327e3f2c" />



<img width="1053" height="887" alt="image" src="https://github.com/user-attachments/assets/99d2b90c-f826-42df-a643-c82efeb19d7e" />


## Notes

Test Cases are paralelly run in 3 cores
Retry failed Test 2 Times
