package utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ManagementOnSchoolsAuthentication {

    public static String generateToken() {
        String payLoad = "{\"password\": \"John.123\", \"username\": \"AdminJohn\"}";
        Response response = given().contentType(ContentType.JSON).body(payLoad).post("https://managementonschools.com/app/auth/login");
        return response.jsonPath().getString("token");
    }


}
