package gr.registry.service;

import gr.registry.domain.Citizen;
import gr.registry.domain.Gender;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CitizenEntityValidationTest {

    private final Validator validator;

    public CitizenEntityValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validCitizenPassesValidation() {
        Citizen c = new Citizen("AB12CD34","Nikos","Papadopoulos", Gender.MALE, LocalDate.of(1990,1,1),"123456789","Athens");
        assertTrue(validator.validate(c).isEmpty());
    }

    @Test
    void invalidAtRejected() {
        Citizen c = new Citizen("123", "A","B", Gender.OTHER, LocalDate.now(), null, null);
        assertFalse(validator.validate(c).isEmpty());
    }

    @Test
    void invalidAfmRejected() {
        Citizen c = new Citizen("AB12CD34","A","B", Gender.FEMALE, LocalDate.now(),"123","Addr");
        assertFalse(validator.validate(c).isEmpty());
    }
}
