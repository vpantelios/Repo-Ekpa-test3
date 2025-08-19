package gr.registry.service.core;

import gr.registry.domain.Citizen;
import gr.registry.domain.Gender;
import gr.registry.service.repo.CitizenRepository;
import gr.registry.service.repo.CitizenSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CitizenService {
    private final CitizenRepository repo;

    public CitizenService(CitizenRepository repo) {
        this.repo = repo;
    }

    /** Δημιουργία νέου πολίτη με έλεγχο μοναδικού AT. */
    @Transactional
    public Citizen create(Citizen c) {
        if (repo.existsByAt(c.getAt())) {
            throw new IllegalArgumentException("Υπάρχει ήδη πολίτης με αυτόν τον ΑΤ");
        }
        return repo.save(c);
    }

    /** Διαγραφή βάσει AT. */
    @Transactional
    public void deleteByAt(String at) {
        Optional<Citizen> existing = repo.findByAt(at);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Δεν βρέθηκε πολίτης με ΑΤ: " + at);
        }
        repo.delete(existing.get());
    }

    /** Ενημέρωση μόνο ΑΦΜ & Διεύθυνσης. */
    @Transactional
    public Citizen updateFields(String at, String afm, String address) {
        Citizen c = repo.findByAt(at).orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε πολίτης με ΑΤ: " + at));
        if (afm != null) c.setAfm(afm);
        if (address != null) c.setAddress(address);
        return repo.save(c);
    }

    public Citizen getByAt(String at) {
        return repo.findByAt(at).orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε πολίτης με ΑΤ: " + at));
    }

    public List<Citizen> search(String at, String firstName, String lastName, Gender gender, LocalDate birthDate, String afm, String address) {
        Specification<Citizen> spec = CitizenSpecifications.byFilters(at, firstName, lastName, gender, birthDate, afm, address);
        return repo.findAll(spec);
    }
}
