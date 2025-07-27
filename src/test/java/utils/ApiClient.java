package utils;

import static io.restassured.RestAssured.*;

import java.util.Map;

public class ApiClient {

    private static String token;

    // This method use for setup Base URL and Token
    public static void setup() {
        baseURI = "https://restful-booker.herokuapp.com";
        token = AuthenticationHelper.getAuthToken();
    }
}
