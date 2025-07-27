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

<img width="1898" height="887" alt="image" src="https://github.com/user-attachments/assets/75e99ac4-00e9-4d38-8f45-bbf21f43c96b" />


<img width="1062" height="872" alt="image" src="https://github.com/user-attachments/assets/17257e00-bf5e-44ad-afa3-88efee1af230" />

