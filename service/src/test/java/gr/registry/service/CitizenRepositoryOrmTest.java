package gr.registry.service;

import gr.registry.domain.Citizen;
import gr.registry.domain.Gender;
import gr.registry.service.repo.CitizenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CitizenRepositoryOrmTest {

    @Autowired
    CitizenRepository repo;

    @Test
    void saveAndFindByAt() {
        Citizen c = new Citizen("ZZ99YY88","Test","User", Gender.OTHER, LocalDate.of(2000,2,2), null, null);
        repo.save(c);
        assertTrue(repo.existsByAt("ZZ99YY88"));
        assertTrue(repo.findByAt("ZZ99YY88").isPresent());
    }
}
