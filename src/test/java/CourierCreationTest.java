import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.CourierRequestCreation;
import model.LoginResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("Courier Creation Tests for Courier API")
public class CourierCreationTest {
    private static final CourierSteps COURIER_CHECK = new CourierSteps();
    private String uniqueLogin;
    private String password;
    private String uniqueFirstName;

    @Before
    public void initialization() {
        uniqueLogin = COURIER_CHECK.generateUniqueLogin();
        password = COURIER_CHECK.generatePassword();
        uniqueFirstName = COURIER_CHECK.generateFirstName();
    }

    @Test
    @DisplayName("Create a Courier")
    @Description("Verify the response about a successful creation of a courier")
    public void testCourierCreation() {

        CourierRequestCreation courierCreateRequest = new CourierRequestCreation(uniqueLogin, password, uniqueFirstName);
        Response response = COURIER_CHECK.createCourier(courierCreateRequest);
        response.then()
                .assertThat()
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Attempt to Create Duplicate Courier")
    @Description("Verify the message that creating two couriers with the same login is not allowed")
    public void testDuplicateCourierCreation() {

        CourierRequestCreation courierCreateRequest = new CourierRequestCreation(uniqueLogin, password, uniqueFirstName);

        COURIER_CHECK.createCourier(courierCreateRequest);
        COURIER_CHECK.createCourier(courierCreateRequest)
                .then()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Create Courier with Missing Mandatory Fields")
    @Description("Verify the message that all mandatory fields are required to create a courier")
    public void testCreatingCourierWithMissingFields() {
        CourierRequestCreation courierCreateRequest = new CourierRequestCreation(uniqueLogin);

        COURIER_CHECK.createCourier(courierCreateRequest)
                .then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create Courier and Validate Response Code")
    @Description("Verify that the API responds with the correct status code")
    public void testCreatingCourierAndValidatingResponseCode() {


        CourierRequestCreation courierCreateRequest = new CourierRequestCreation(uniqueLogin, password, uniqueFirstName);
        COURIER_CHECK.createCourier(courierCreateRequest)
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    @DisplayName("Create Courier and Verify Successful Response")
    @Description("Verify that a successful courier creation response contains 'ok: true'")
    public void testCreateCourierAndVerifySuccessResponse() {


        CourierRequestCreation courierCreateRequest = new CourierRequestCreation(uniqueLogin, password, uniqueFirstName);

        COURIER_CHECK.createCourier(courierCreateRequest).then()
                .assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Create Courier with Missing Field and Verify Error Response")
    @Description("Check the status code where creating a courier with a missing field results in an error response")
    public void testCreateCourierWithMissingFieldAndVerifyError() {


        CourierRequestCreation courierCreateRequest = new CourierRequestCreation(password, uniqueFirstName);
        COURIER_CHECK.createCourier(courierCreateRequest)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Attempt to Create Courier with Existing Login")
    @Description("Verify the status code that creating a courier with an existing login results in an error response")
    public void testCreateCourierWithExistingLoginAndVerifyError() {

        CourierRequestCreation courierCreateRequest = new CourierRequestCreation(uniqueLogin, password, uniqueFirstName);
        COURIER_CHECK.createCourier(courierCreateRequest);
        COURIER_CHECK.createCourier(courierCreateRequest)
                .then()
                .assertThat()
                .statusCode(409);

    }

    @After
    public void deleteCourier() {

        Response response = COURIER_CHECK.loginCourier(uniqueLogin, password);

        if (response.getStatusCode() == 200) {
            LoginResponse loginResponse = response.body().as(LoginResponse.class);
            COURIER_CHECK.deleteCourier(loginResponse.getId());
        }

    }

}