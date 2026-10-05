# QA Automation Assignment

UI tests for [SauceDemo](https://www.saucedemo.com/) (Selenide) and API tests for [Restful Booker](https://restful-booker.herokuapp.com/apidoc/index.html) (REST Assured).

Stack: Java 25, Maven, JUnit 6, Selenide, REST Assured, AssertJ, Docker.

## Prerequisites

To run locally:

- JDK 25
- Maven 3.9+
- Google Chrome (the matching ChromeDriver is downloaded automatically)

To run in Docker, only Docker is needed.

Both apps are public websites, so the tests need internet access.

## Running locally

All tests:

```bash
mvn test
```

UI tests:

```bash
mvn test -Dgroups=ui
```

API tests:

```bash
mvn test -Dgroups=api
```

UI tests run headless by default. To watch the browser, add `-Dselenide.headless=false`.

Test reports are in `target/surefire-reports`. For failed UI checks, screenshots and page sources are saved to `target/selenide-reports`.

## Running in Docker

```bash
docker build -t qa-automation-tests .
docker run --rm qa-automation-tests
```

To run only one group:

```bash
docker run --rm qa-automation-tests test -Dgroups=ui
docker run --rm qa-automation-tests test -Dgroups=api
```

The image is based on the official Maven image with JDK 25. Chromium and ChromeDriver are installed from the Debian repositories, so the image also works on Apple Silicon. Dependencies are downloaded during the build.

## Project structure

```
src/test/java/com/qa/automation
├── config        reads config.properties
├── ui
│   ├── pages     page objects: login, products, cart
│   ├── model     Product, SortOption
│   └── LoginTest, ProductCartTest
└── api
    ├── client    request specification, BookingClient
    ├── model     Booking, BookingDates, CreatedBooking
    └── BookingApiTest
```

Settings live in `src/test/resources`: `selenide.properties` for the browser and `config.properties` for credentials and the API URL. Any value can be overridden with `-D`, for example `-Dselenide.timeout=10000`.

## Test approach

**Locators.** SauceDemo has `data-test` attributes on all the elements I needed, so I used only those. They exist for testing and don't change when styling or text changes. To click a product's button, I first find the product card by the product name and then search inside that card, so the test can't click a button of a different product.

**Synchronization.** There are no sleeps or explicit waits. All checks are Selenide conditions, which retry until the timeout. After each action I wait for a visible sign that it has finished before reading anything from the page: the sort label changes, the "Add to cart" button turns into "Remove", and after navigation the URL and the page title are checked.

**Code structure.** UI tests use page objects. Page methods return the next page, so a test reads like the user flow. The sorting test is parameterized over all four sort options, and each run adds the first product after sorting to the cart.

API calls go through a small client that returns raw responses. The same client methods are used in positive and negative tests, and the assertions stay in the tests. Request and response bodies are Java records mapped with Jackson. The created booking is compared field by field with AssertJ's recursive comparison.

Tests don't depend on each other. Each UI test gets a fresh browser, because SauceDemo keeps the cart in local storage. Each API test creates its own booking with unique data and deletes it afterwards.

**Assumptions and limitations.**

- Both apps are shared public demo environments, so a test can occasionally fail because the service is slow or down. I didn't add retries, so they can't hide real failures.
- Restful Booker doesn't always follow REST conventions. It returns 200 on create and 201 on delete, and it requires exactly `Accept: application/json` (otherwise it answers 418). The tests check the actual behaviour.
- Creating a booking with invalid data returns 500 instead of a 4xx. I treated that as a bug in the API and didn't write a test that expects it.
- Only Chrome is covered.
