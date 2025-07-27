package api.negative;

import api.integration.BaseApiTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.RetryExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

@ExtendWith(RetryExtension.class)
public class NegativeBookingTests extends BaseApiTest {

    @BeforeAll
    public static void setup() {
        BaseApiTest.setup();
    }

    @Description("Validates that the API returns an error (status code 500) when an invalid booking payload is submitted.")
    @Test
    public void testCreateBookingWithInvalidPayload() {
        given()
                .spec(getRequestSpec())
                .body(invalidPayload)
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(500);
    }

    @Description("Checks that creating a booking with special characters in the payload is handled gracefully by the API, returning either a client error (400) or server error (500).")
    @Test
    public void testCreateBookingWithSpecialCharacters() {
        given()
                .spec(getRequestSpec())
                .body(specialCharacterPayload)
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(anyOf(is(400), is(500)));
    }

    @Description("Tests that the API properly handles and rejects attempts at SQL injection via query parameters, expecting a 400 or 500 error response.")
    @Test
    public void testGetBookingWithSQLInjectionAttempt() {
        given()
                .queryParam("firstname", sqlInjectionQuery.get("firstname"))
                .queryParam("lastname", sqlInjectionQuery.get("lastname"))
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(anyOf(is(400), is(500)));
    }

    @Description("Ensures that the API returns an error when attempting to create a booking with missing mandatory fields, returning either a 400 or 500 status code.")
    @Test
    public void testCreateBookingWithMissingRequiredFields() {
        given()
                .spec(getRequestSpec())
                .body(missingFieldsPayload)
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(anyOf(is(400), is(500)));
    }
}
