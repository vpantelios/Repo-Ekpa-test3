package gr.registry.it;

import gr.registry.service.RegistryServiceApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.springframework.test.context.ActiveProfiles;
@ActiveProfiles("test")
@SpringBootTest(classes = RegistryServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class CitizenApiIT {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void fullCrudFlow() {
        final String at = "AT123456";

        // CREATE 
        given()
            .contentType("application/json")
            .body("""
                {
                  "at":"AT123456",
                  "firstName":"Giorgos",
                  "lastName":"Papadopoulos",
                  "gender":"MALE",
                  "birthDate":"01-05-1990",
                  "afm":"123456789",
                  "address":"Athens"
                }
                """)
        .when().post("/citizens")
        .then().statusCode(201);

        // GET 
        given().when().get("/citizens/" + at)
        .then().statusCode(200)
               .body("firstName", equalTo("Giorgos"))
               .body("lastName", equalTo("Papadopoulos"));

        // Duplicate CREATE 
        given().contentType("application/json")
               .body("""
                {
                  "at":"AT123456",
                  "firstName":"Giorgos",
                  "lastName":"Papadopoulos",
                  "gender":"MALE",
                  "birthDate":"01-05-1990",
                  "afm":"123456789",
                  "address":"Athens"
                }
               """)
        .when().post("/citizens")
        .then().statusCode(400)
               .body("$", anyOf(hasKey("error"), hasKey("message")));

        // SEARCH -> 200, >=1
        given().when().get("/citizens/search?lastName=papad")
        .then().statusCode(200)
               .body("size()", greaterThanOrEqualTo(1));

     // PATCH (afm + address) -> 200
        given()
          .contentType("application/json")
          .body("{\"afm\":\"987654321\",\"address\":\"Thessaloniki\"}")
        .when()
          .patch("/citizens/" + at)
        .then()
          .statusCode(200)
          .body("afm", equalTo("987654321"))
          .body("address", equalTo("Thessaloniki"));


        // DELETE 
        given().when().delete("/citizens/" + at)
        .then().statusCode(204);

        // GET after delete 
        given().when().get("/citizens/" + at)
        .then().statusCode(anyOf(is(404), is(400)));
    }
}
