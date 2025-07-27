package utils;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthenticationHelper {

    private static String token;
    //This Method returns valid authentication token.
    public static String getAuthToken() {
        if (token == null) {
            token = generateToken();
        }
        return token;
    }
    //This Method Use for generate token and Credentials are loaded from the 'authCredentials.json' test data file
    private static String generateToken() {
        Map<String, Object> credentials = TestDataLoader.loadTestData("authCredentials.json");

        return given()
                .contentType("application/json")
                .body(credentials)
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
