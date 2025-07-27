package api.integration;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import api.integration.BaseApiTest;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.RetryExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(RetryExtension.class)
public class BookingE2ETests extends BaseApiTest {

    @BeforeAll
    public static void setup() {
        BaseApiTest.setup();
    }

    /**
     * This Test Verifies End To End Booking Flow By Create, Update, Delete And Verify Delete By Get.
     */
    @Test
    @Description("Verify full E2E booking lifecycle: Create → Update → Delete → Verify Deletion")
    public void endToEndBookingFlow() {
        // Create Booking
        Response createResponse = given()
                .spec(getRequestSpec())
                .body(createBookingData)
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("booking.firstname", equalTo(createBookingData.get("firstname")))
                .body("booking.lastname", equalTo(createBookingData.get("lastname")))
                .body("booking.totalprice", equalTo(createBookingData.get("totalprice")))
                .extract().response();

        int bookingId = createResponse.path("bookingid");
        System.out.println("Created booking ID: " + bookingId);

        Map<String, Object> payload = (Map<String, Object>) updateBookingData.get("updateSingleField");

        // Update Booking
        given()
                .spec(getAuthenticatedRequestSpec())
                .body(payload)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo(payload.get("firstname")));
        System.out.println("Updated First Name: " + payload.get("firstname"));

        // Delete Booking
        given()
                .spec(getAuthenticatedRequestSpec())
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(201), is(204)));
        System.out.println("Deleted booking ID: " + bookingId);

        // Verify Deleted Booking
        given()
                .when()
                .get("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(404);
    }

    /**
     * This Test Verifies Booking Create Filter Update Work Flow By Bulk Booking Creation, Filter by firstname And Update each filtered booking.
     */
    @Test
    @Description("Bulk create bookings, filter by firstname, and update each filtered booking")
    public void bulkBookingCreateFilterUpdateFlow() {
        List<Integer> createdIds = new ArrayList<>();

        // Bulk Booking Creation
        for (Map<String, Object> booking : bulkBookingsData) {
            Response response = given()
                    .spec(getRequestSpec())
                    .body(booking)
                    .when()
                    .post("/booking")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .extract().response();

            int id = response.path("bookingid");
            createdIds.add(id);
            System.out.println("Created booking ID: " + id);
        }

        System.out.println("Created test booking IDs: " + createdIds);

        //Filter by firstname
        Response filterResponse = given()
                .queryParam("firstname", "BulkUser")
                .when()
                .get("/booking")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();

        List<Integer> filteredBookingIds = filterResponse.jsonPath().getList("bookingid");
        System.out.println("Filtered booking IDs: " + filteredBookingIds);

        if (filteredBookingIds == null || filteredBookingIds.isEmpty()) {
            System.err.println("No bookings found with firstname = 'BulkUser'. Please check your test data or API filtering logic.");
            return;
        }

        // Update each filtered booking
        Map<String, Object> updatePayload = (Map<String, Object>) updateBookingData.get("updateMultipleFields");

        for (int id : filteredBookingIds) {
            given()
                    .spec(getAuthenticatedRequestSpec())
                    .body(updatePayload)
                    .when()
                    .put("/booking/" + id)
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("firstname", equalTo(updatePayload.get("firstname")))
                    .body("lastname", equalTo(updatePayload.get("lastname")))
                    .body("totalprice", equalTo(updatePayload.get("totalprice")));

            System.out.println("Updated booking ID: " + id);
        }
    }

    /**
     * This Test Verifies End To End Booking Flow With Consistency Check.
     */
    @Test
    @Description("E2E booking test with cross-endpoint consistency check for data accuracy")
    public void endToEndBookingFlowWithConsistencyCheck() {
        // Create Booking
        Response createResponse = given()
                .spec(getRequestSpec())
                .body(createBookingData)
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("booking.firstname", equalTo(createBookingData.get("firstname")))
                .body("booking.lastname", equalTo(createBookingData.get("lastname")))
                .body("booking.totalprice", equalTo(createBookingData.get("totalprice")))
                .extract().response();

        int bookingId = createResponse.path("bookingid");
        System.out.println("Created booking ID: " + bookingId);

        // GET Cross  endpoint data consistency
        Response getResponse = given()
                .when()
                .get("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        // Validate fields consistency
        Map<String, Object> getBooking = getResponse.as(Map.class);

        assert createBookingData.get("firstname").equals(getBooking.get("firstname"));
        assert createBookingData.get("lastname").equals(getBooking.get("lastname"));
        assert createBookingData.get("totalprice").equals(getBooking.get("totalprice"));
        assert createBookingData.get("depositpaid").equals(getBooking.get("depositpaid"));
        assert createBookingData.get("additionalneeds").equals(getBooking.get("additionalneeds"));

        Map<String, String> expectedDates = (Map<String, String>) createBookingData.get("bookingdates");
        Map<String, String> actualDates = (Map<String, String>) getBooking.get("bookingdates");

        assert expectedDates.get("checkin").equals(actualDates.get("checkin"));
        assert expectedDates.get("checkout").equals(actualDates.get("checkout"));

        System.out.println("Cross-endpoint data consistency verified.");

        // Update Booking
        Map<String, Object> payload = (Map<String, Object>) updateBookingData.get("updateSingleField");

        given()
                .spec(getAuthenticatedRequestSpec())
                .body(payload)
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo(payload.get("firstname")));

        System.out.println("Updated First Name: " + payload.get("firstname"));

        // Delete Booking
        given()
                .spec(getAuthenticatedRequestSpec())
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(201), is(204)));

        System.out.println("Deleted booking ID: " + bookingId);

        // Verify Deleted Booking
        given()
                .when()
                .get("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(404);

        System.out.println("Verified deletion: Booking ID " + bookingId + " not found (404).");
    }
}
