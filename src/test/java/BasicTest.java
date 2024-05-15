import io.restassured.RestAssured;
import org.junit.Before;

public class BasicTest {

    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Before
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

}
