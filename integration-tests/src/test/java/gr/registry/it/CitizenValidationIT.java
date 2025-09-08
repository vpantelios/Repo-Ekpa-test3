package gr.registry.it;

import gr.registry.service.RegistryServiceApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@ActiveProfiles("test")
@SpringBootTest(classes = RegistryServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)


class CitizenValidationIT {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        // Αν “σπάσει” assertion τύπωσε όλο το request/response
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private String baseValidJson() {
        return """
        {
          "at":"AT123456",
          "firstName":"Giorgos",
          "lastName":"Papadopoulos",
          "gender":"MALE",
          "birthDate":"01-05-1990",
          "afm":"123456789",
          "address":"Athens"
        }
        """;
    }

    @Test
    void invalidBirthDate_returns400_withClearMessage() {
        // birthDate σε λάθος format (π.χ. 1990-05-01 αντί για DD-MM-YYYY)
        String payload = baseValidJson().replace("\"01-05-1990\"", "\"1990-05-01\"");

        given()
          .contentType("application/json")
          .body(payload)
        .when()
          .post("/citizens")
        .then()
          .statusCode(400)
          .body("$",anyOf(hasKey("error"), hasKey("message")))
          .body(containsString("DD-MM-YYYY"));
    }

    @Test
    void shortAfm_returns400_withClearMessage() {
        String payload = baseValidJson().replace("\"123456789\"", "\"12345\""); // πολύ μικρό AFM

        given()
          .contentType("application/json")
          .body(payload)
        .when()
          .post("/citizens")
        .then()
          .statusCode(400)
          .body("$",anyOf(hasKey("error"), hasKey("message")))
          .body(containsString("9")); 
    }

    @Test
    void invalidAt_returns400_withClearMessage() {
        
        String payload = baseValidJson().replace("\"AT123456\"", "\"AT12BC34\"");

        given()
          .contentType("application/json")
          .body(payload)
        .when()
          .post("/citizens")
        .then()
          .statusCode(400)
          .body("$",anyOf(hasKey("error"), hasKey("message")))
          .body(containsString("AT")); // π.χ. "Μη έγκυρο AT (2 γράμματα + 6 ψηφία)"
    }
}
