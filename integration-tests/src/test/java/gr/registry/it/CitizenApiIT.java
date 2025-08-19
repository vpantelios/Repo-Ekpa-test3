package gr.registry.it;

import gr.registry.service.RegistryServiceApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = RegistryServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CitizenApiIT {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void fullCrudFlow() {
        // Create
        String body = "{\n" +
                "  \"at\": \"AT12BC34\",\n" +
                "  \"firstName\": \"Giorgos\",\n" +
                "  \"lastName\": \"Ioannou\",\n" +
                "  \"gender\": \"MALE\",\n" +
                "  \"birthDate\": \"12-11-2008\",\n" +
                "  \"afm\": \"123456789\",\n" +
                "  \"address\": \"Athens\"\n" +
                "}";
        given().contentType("application/json").body(body)
                .when().post("/citizens")
                .then().statusCode(201)
                .body("at", equalTo("AT12BC34"));

        // Duplicate should fail
        given().contentType("application/json").body(body)
                .when().post("/citizens")
                .then().statusCode(400)
                .body("error", containsString("Υπάρχει ήδη"));

        // Get
        given().when().get("/citizens/AT12BC34")
                .then().statusCode(200)
                .body("firstName", equalTo("Giorgos"));

        // Search by lastName contains
        given().when().get("/citizens/search?lastName=oann")
                .then().statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        // Update AFM & address
        String upd = "{ \"afm\": \"987654321\", \"address\": \"Thessaloniki\" }";
        given().contentType("application/json").body(upd)
                .when().patch("/citizens/AT12BC34")
                .then().statusCode(200)
                .body("afm", equalTo("987654321"))
                .body("address", equalTo("Thessaloniki"));

        // Delete
        given().when().delete("/citizens/AT12BC34")
                .then().statusCode(204);

        // Get after delete -> fail
        given().when().get("/citizens/AT12BC34")
                .then().statusCode(400);
    }
}
