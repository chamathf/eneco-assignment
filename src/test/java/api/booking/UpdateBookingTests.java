package api.booking;

import api.integration.BaseApiTest;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.RetryExtension;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(RetryExtension.class)
public class UpdateBookingTests extends BaseApiTest {

    @BeforeAll
    public static void setupAll() {
        BaseApiTest.setup();
    }

    /**
     * This Test Verifies updating a single field of a booking
     */
    @Test
    @Description("Verify partial update of a booking using PATCH with a single field change.")
    public void testUpdateSingleField() {
        given()
                .spec(getAuthenticatedRequestSpec())
                .body(updateSingleField)
                .when()
                .patch("/booking/" + validBookingId)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo(updateSingleField.get("firstname")))
                .body("lastname", equalTo(updateSingleField.get("lastname")))
                .body("totalprice", equalTo(updateSingleField.get("totalprice")));
    }

    /**
     * This Test Verifies updating multiple fields of a booking
     */
    @Test
    @Description("Verify full update of a booking using PUT with multiple fields.")
    public void testUpdateMultipleFields() {
        given()
                .spec(getAuthenticatedRequestSpec())
                .body(updateMultipleFields)
                .when()
                .put("/booking/" + validBookingId)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo(updateMultipleFields.get("firstname")))
                .body("lastname", equalTo(updateMultipleFields.get("lastname")))
                .body("totalprice", equalTo(updateMultipleFields.get("totalprice")))
                .body("depositpaid", equalTo(updateMultipleFields.get("depositpaid")))
                .body("additionalneeds", equalTo(updateMultipleFields.get("additionalneeds")));
    }

    /**
     * This Test Verifies updating nested booking dates
     */
    @Test
    @Description("Verify update of nested fields (booking dates) using PUT.")
    public void testUpdateNestedObjects() {
        given()
                .spec(getAuthenticatedRequestSpec())
                .body(updateBookingDatesOnly)
                .when()
                .put("/booking/" + validBookingId)
                .then()
                .log().all()
                .statusCode(200)
                .body("bookingdates.checkin", equalTo(((Map<String, String>) updateBookingDatesOnly.get("bookingdates")).get("checkin")))
                .body("bookingdates.checkout", equalTo(((Map<String, String>) updateBookingDatesOnly.get("bookingdates")).get("checkout")));
    }

    /**
     * This Test Verifies non-updated fields remain unchanged
     */
    @Test
    @Description("Verify non-updated fields in a booking remain unchanged after update.")
    public void testVerifyNonUpdatedFieldsRemainUnchanged() {
        Map<String, Object> original = given()
                .spec(getAuthenticatedRequestSpec())
                .when()
                .get("/booking/" + validBookingId)
                .then()
                .statusCode(200)
                .extract()
                .as(Map.class);

        original.put("firstname", "NewName");

        given()
                .spec(getAuthenticatedRequestSpec())
                .body(original)
                .when()
                .put("/booking/" + validBookingId)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo("NewName"))
                .body("lastname", equalTo(original.get("lastname")))
                .body("totalprice", equalTo(original.get("totalprice")));
    }

    /**
     * This Test Verifies updating with an invalid booking ID returns 404
     */
    @Test
    @Description("Verify that updating with an invalid booking ID returns 404 Not Found.")
    public void testInvalidBookingIdReturns404() {
        given()
                .spec(getAuthenticatedRequestSpec())
                .body(updateSingleField)
                .when()
                .patch("/booking/" + invalidBookingId)
                .then()
                .log().all()
                .statusCode(404);
    }

    /**
     * This Test Verifies updating with invalid data type returns error
     */
    @Test
    @Description("Verify that updating with invalid data types returns error (400 or 500).")
    public void testInvalidDataTypeReturnsError() {
        given()
                .spec(getAuthenticatedRequestSpec())
                .body(invalidDataType)
                .when()
                .patch("/booking/" + validBookingId)
                .then()
                .log().all()
                .statusCode(anyOf(is(400), is(500)));
    }

    /**
     * This Test Verifies updating without authentication returns 401
     */
    @Test
    @Description("Verify that updating a booking without authentication returns 401 Unauthorized.")
    public void testMissingAuthReturns401() {
        given()
                .spec(getRequestSpec())
                .body(updateSingleField)
                .when()
                .patch("/booking/" + validBookingId)
                .then()
                .statusCode(401);
    }

    /**
     * This Test Verifies idempotent update behavior
     */
    @Test
    @Description("Verify that repeated updates with the same data return consistent results (idempotency).")
    public void testIdempotentUpdate() {

        given()
                .spec(getAuthenticatedRequestSpec())
                .body(updateMultipleFields)
                .when()
                .put("/booking/" + validBookingId)
                .then()
                .statusCode(200);

        given()
                .spec(getAuthenticatedRequestSpec())
                .body(updateMultipleFields)
                .when()
                .put("/booking/" + validBookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(updateMultipleFields.get("firstname")));
    }
}
