package api.booking;

import api.integration.BaseApiTest;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetBookingTests extends BaseApiTest {

    @BeforeAll
    public static void setup() {
        BaseApiTest.setup();
    }

    /**
     * This Test Verifies GET /booking returns a non-empty list of booking IDs with status 200.
     */
    @Test
    @Description("Verify GET /booking returns a non-empty list of booking IDs.")
    public void testRetrieveAllBookingIds() {
        given()
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("bookingid", not(empty()));
    }

    /**
     * This Test Verifies GET /booking with firstname filter returns a non-empty result and status 200.
     */
    @Test
    @Description("Verify GET /booking with firstname filter returns valid data.")
    public void testFilterByFirstname() {
        given()
                .queryParam("firstname", getBookingTestData.get("firstname"))
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", not(empty()));
    }

    /**
     * This Test Verifies GET /booking with lastname filter returns a non-empty result and status 200.
     */
    @Test
    @Description("Verify GET /booking with lastname filter returns valid data.")
    public void testFilterByLastname() {
        given()
                .queryParam("lastname", getBookingTestData.get("lastname"))
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", not(empty()));
    }

    /**
     * This Test Verifies GET /booking with Checkin Date filter returns a non-empty result and status 200.
     */
    @Test
    @Description("Verify GET /booking with checkin date filter returns valid data.")
    public void testFilterByCheckinDate() {
        given()
                .queryParam("checkin", getBookingTestData.get("checkin"))
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", not(empty()));
    }

    /**
     * This Test Verifies GET /booking with Checkout Date filter returns a non-empty result and status 200.
     */
    @Test
    @Description("Verify GET /booking with checkout date filter returns valid data.")
    public void testFilterByCheckoutDate() {
        given()
                .queryParam("checkout", getBookingTestData.get("checkout"))
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", not(empty()));
    }

    /**
     * This Test Verifies GET /booking with Combined filters returns a non-empty result and status 200.
     */
    @Test
    @Description("Verify GET /booking with multiple filters returns valid data.")
    public void testSupportCombinedFilters() {
        given()
                .queryParam("firstname", getBookingTestData.get("firstname"))
                .queryParam("lastname", getBookingTestData.get("lastname"))
                .queryParam("checkin", getBookingTestData.get("checkin"))
                .queryParam("checkout", getBookingTestData.get("checkout"))
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", not(empty()));
    }

    /**
     * This Test Verifies that an invalid checkin date format returns a 400 or 500 error status.
     */
    @Test
    @Description("Verify invalid checking date format returns 400 or 500.")
    public void testErrorForInvalidDateFormat() {
        given()
                .queryParam("checkin", getBookingTestData.get("invalidCheckinDate"))
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(anyOf(is(400), is(500)));
    }

    /**
     * This Test Verifies that an invalid Parameter Type format returns a 200 status with an empty body.
     */
    @Test
    @Description("Verify invalid parameter value returns 200 with empty response.")
    public void testErrorForInvalidParameterType() {
        given()
                .queryParam("firstname", getBookingTestData.get("invalidCheckinDate"))
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", empty());
    }

    /**
     * This Test Verifies that the response time for GET /booking is less than 2 seconds.
     */
    @Test
    @Description("Verify GET /booking responds in under 2 seconds.")
    public void testResponseTimeLessThanTwoSeconds() {
        given()
                .when()
                .get("/booking")
                .then()
                .log().all()
                .time(lessThan(2000L));
    }

    /**
     * This Test Verifies GET /booking returns a Valid Response Structure and each booking id is an Integer.
     */
    @Test
    @Description("Verify response structure and booking ID data type.")
    public void testResponseStructureAndDataType() {
        given()
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", not(empty()))
                .body("bookingid", everyItem(instanceOf(Integer.class)));
    }
}
