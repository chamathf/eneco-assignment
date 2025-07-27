package api.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ApiClient;
import utils.AuthenticationHelper;
import utils.TestDataLoader;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseApiTest {
    protected static Map<String, Object> getBookingData;
    protected static Map<String, Object> createBookingData;
    protected static Map<String, Object> updateBookingData;
    protected static List<Map<String, Object>> bulkBookingsData;
    protected static Map<String, Object> negativeBookingData;
    protected static Map<String, Object> specialCharacterPayload;
    protected static Map<String, Object> invalidPayload;
    protected static Map<String, Object> sqlInjectionQuery;
    protected static Map<String, Object> missingFieldsPayload;
    protected static Map<String, Object> deleteBookingTestData;
    protected static Map<String, Object> getBookingTestData;
    protected static Map<String, Object> updateBookingTestData;
    protected static Map<String, Object> updateSingleField;
    protected static Map<String, Object> updateMultipleFields;
    protected static Map<String, Object> updateBookingDatesOnly;
    protected static Map<String, Object> invalidDataType;
    protected static int validBookingId;
    protected static int invalidBookingId;
    protected static int bookingIdToDelete;
    protected static String token;

    //This Method Use for Initializes common test data, sets up authentication, and prepares reusable request specifications.
    public static void setup() {
        ApiClient.setup();
        getBookingData = TestDataLoader.loadTestData("getBooking.json");
        createBookingData = TestDataLoader.loadTestData("createBooking.json");
        updateBookingData = TestDataLoader.loadTestData("updateBooking.json");
        Map<String, Object> bulkData = TestDataLoader.loadTestData("bulkCreationBooking.json");
        bulkBookingsData = (List<Map<String, Object>>) bulkData.get("bulkBookings");
        negativeBookingData = TestDataLoader.loadTestData("negativeBooking.json");
        invalidPayload = (Map<String, Object>) negativeBookingData.get("invalidDataType");
        specialCharacterPayload = (Map<String, Object>) negativeBookingData.get("specialCharacters");
        sqlInjectionQuery = (Map<String, Object>) negativeBookingData.get("sqlInjectionQuery");
        missingFieldsPayload = (Map<String, Object>) negativeBookingData.get("missingFields");
        deleteBookingTestData = TestDataLoader.loadTestData("deleteBooking.json");
        getBookingTestData = TestDataLoader.loadTestData("getBooking.json");
        updateBookingTestData = TestDataLoader.loadTestData("updateBooking.json");
        updateSingleField = (Map<String, Object>) updateBookingTestData.get("updateSingleField");
        updateMultipleFields = (Map<String, Object>) updateBookingTestData.get("updateMultipleFields");
        updateBookingDatesOnly = (Map<String, Object>) updateBookingTestData.get("updateBookingDatesOnly");
        invalidDataType = (Map<String, Object>) updateBookingTestData.get("invalidDataType");
        invalidBookingId = (int) updateBookingTestData.get("invalidBookingId");
        Map<String, Object> payload = (Map<String, Object>) deleteBookingTestData.get("deleteBookingPayload");
        token = AuthenticationHelper.getAuthToken();
        System.out.println("Token ID: " + token);

        // Create booking for DeleteBookingTests
        Response deleteResponse = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post("/booking");

        if (deleteResponse.getStatusCode() != 200) {
            System.err.println("Failed to create booking for DeleteBookingTests: " + deleteResponse.getStatusLine());
        }
        bookingIdToDelete = deleteResponse.then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        System.out.println("Booking ID to delete: " + bookingIdToDelete);

        // Create booking for UpdateBookingTests
        Response updateResponse = given()
                .header("Content-Type", "application/json")
                .body(createBookingData)
                .post("/booking");

        if (updateResponse.getStatusCode() != 200) {
            System.err.println("Failed to create booking for UpdateBookingTests: " + updateResponse.getStatusLine());
        }
        validBookingId = updateResponse.then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        System.out.println("Valid Booking ID for updates: " + validBookingId);
    }

    protected RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    protected RequestSpecification getAuthenticatedRequestSpec() {
        return getRequestSpec()
                .cookie("token", token);
    }
}