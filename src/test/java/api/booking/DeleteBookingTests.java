package api.booking;

import api.integration.BaseApiTest;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.RetryExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(RetryExtension.class)
public class DeleteBookingTests extends BaseApiTest {

    @BeforeAll
    public static void setupAll() {
        BaseApiTest.setup();
    }

    /**
     * This Test Verifies Delete existing booking with valid token and ID
     */
    @Test
    @Description("Verify deleting a valid existing booking with a valid token and ID returns 200 or 201")
    public void testDeleteValidExistingBooking() {
        given()
                .spec(getAuthenticatedRequestSpec())
                .when()
                .delete("/booking/" + bookingIdToDelete)
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(201)));
    }

    /**
     * This Test Verifies deleted booking returns 404 on GET
     */
    @Test
    @Description("After a booking is deleted, sending a GET request to the same booking ID should return 404 Not Found, confirming the deletion was successful.")
    public void testGetAfterDeleteReturns404() {
        given()
                .spec(getRequestSpec())
                .when()
                .get("/booking/" + bookingIdToDelete)
                .then()
                .log().all()
                .statusCode(404);
    }

    /**
     * This Test Verifies non-existent booking ID should return 404
     */
    @Test
    @Description("Deleting a non-existent booking ID should return 404 Not Found.")
    public void testDeleteNonExistentBookingId() {
        given()
                .spec(getAuthenticatedRequestSpec())
                .when()
                .delete("/booking/" + deleteBookingTestData.get("nonExistentBookingId"))
                .then()
                .log().all()
                .statusCode(404);
    }

    /**
     * This Test Verifies booking with invalid token should return 403
     */
    @Test
    @Description("Attempting to delete with an invalid token should return 403 Forbidden.")
    public void testDeleteWithInvalidToken() {
        given()
                .spec(getRequestSpec())
                .cookie("token", deleteBookingTestData.get("invalidToken"))
                .when()
                .delete("/booking/" + bookingIdToDelete)
                .then()
                .log().all()
                .statusCode(403);
    }

    /**
     * This Test Verifies booking without token should return 403
     */
    @Test
    @Description("Attempting to delete without any token should return 403 Forbidden.")
    public void testDeleteWithoutToken() {
        given()
                .spec(getRequestSpec())
                .when()
                .delete("/booking/" + bookingIdToDelete)
                .then()
                .log().all()
                .statusCode(403);
    }
}
