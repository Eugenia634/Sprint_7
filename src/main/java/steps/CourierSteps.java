package steps;

import constants.Constants;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierRequestCreation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class
CourierSteps {

    @Step("Courier authorization with login '{login}' and password '{password}'")
    public Response loginCourier(String login, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("login", login);
        body.put("password", password);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(Constants.BASE_URL + Constants.LOGIN_URL);
    }

    @Step("Generate Unique Login")
    public String generateUniqueLogin() {
        return "courier_" + System.currentTimeMillis();
    }

    @Step("Generate Password")
    public String generatePassword() {
        Random random = new Random();
        return String.valueOf(1000 + random.nextInt(9000));
    }


    @Step("Generate First Name")
    public String generateFirstName() {
        return "name_" + System.currentTimeMillis();
    }


    @Step("Delete a courier")
    public void deleteCourier(String courierId) {
        Map<String, Object> body = new HashMap<>();
        body.put("id", courierId);

        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .delete(Constants.BASE_URL + Constants.COURIER_URL + courierId)
                .then()
                .statusCode(200);
    }

    @Step("Creating a courier")
    public Response createCourier(CourierRequestCreation courierCreateRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(courierCreateRequest)
                .when()
                .post(Constants.BASE_URL + Constants.COURIER_URL);
    }

}
