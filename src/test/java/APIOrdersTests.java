import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrdersSteps;

import java.util.*;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
@DisplayName("Tests for Orders API")
public class APIOrdersTests {

    private static final OrdersSteps ORDER_HELPER = new OrdersSteps();
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] colors;

    public APIOrdersTests(String firstName, String lastName, String address, int metroStation,
                          String phone, int rentTime, String deliveryDate, String comment, String[] colors) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {"Ivan", "Graf", "Mira 4, 142 apt.", 1, "+7 819 333 33 33", 5, "2022-05-05",
                        "I want a black scooter", new String[]{"BLACK"}},
                {"Masha", "Petrova", "Lenin, 5", 2, "+7 988 987 77 66", 3, "2022-04-04",
                        "I want a grey scooter", new String[]{"GREY"}},
                {"Misha", "Sergeev", "Pushkin 5, 55 apt.", 3, "+7 912 345 22 44", 7, "2022-03-03",
                        "Where is my grey scooter?", new String[]{"BLACK", "GREY"}},
                {"Olga", "Vasin", "Tverskaya 8", 4, "+7 819 676 55 33", 4, "2024-02-02",
                        "I want a scooter without colour", null}
        });
    }

    @Test
    @DisplayName("Creating an order")
    @Description("The test checks the successful creation of an order with 1 color, all colors, or no color specified")
    public void testCreateOrder() {
        Response response = ORDER_HELPER.createOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colors);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
