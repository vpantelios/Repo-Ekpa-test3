package gr.registry.service.web;

import gr.registry.domain.Citizen;
import gr.registry.domain.Gender;
import gr.registry.service.core.CitizenService;
import gr.registry.service.web.dto.CitizenCreateRequest;
import gr.registry.service.web.dto.CitizenUpdateRequest;
import gr.registry.service.web.dto.DateParser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/citizens")
@Validated
public class CitizenController {

    private final CitizenService service;

    public CitizenController(CitizenService service) {
        this.service = service;
    }

    /** Εισαγωγή νέου πολίτη. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Citizen create(@RequestBody @Valid CitizenCreateRequest req) {
        LocalDate birth = DateParser.parseDDMMYYYY(req.getBirthDate());
        Citizen c = new Citizen(req.getAt(), req.getFirstName(), req.getLastName(), req.getGender(), birth, req.getAfm(), req.getAddress());
        return service.create(c);
    }

    /** Διαγραφή εγγραφής με βάση ΑΤ. */
    @DeleteMapping("/{at}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Pattern(regexp = "^[A-Za-z0-9]{8}$") String at) {
        service.deleteByAt(at);
    }

    /** Ενημέρωση μόνο ΑΦΜ & Διεύθυνσης με βάση ΑΤ. */
    @PatchMapping("/{at}")
    public Citizen update(@PathVariable @Pattern(regexp = "^[A-Za-z0-9]{8}$") String at,
                          @RequestBody @Valid CitizenUpdateRequest req) {
        return service.updateFields(at, req.getAfm(), req.getAddress());
    }

    /** Εμφάνιση στοιχείων πολίτη με βάση ΑΤ. */
    @GetMapping("/{at}")
    public Citizen get(@PathVariable @Pattern(regexp = "^[A-Za-z0-9]{8}$") String at) {
        return service.getByAt(at);
    }

    /** Αναζήτηση με οποιονδήποτε συνδυασμό πεδίων. */
    @GetMapping("/search")
    public List<Citizen> search(
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) @Pattern(regexp = "^(|\\d{2}-\\d{2}-\\d{4})$") String birthDate,
            @RequestParam(required = false) @Pattern(regexp = "^(|\\d{9})$") String afm,
            @RequestParam(required = false) String address
    ) {
        LocalDate birth = (birthDate == null || birthDate.isBlank()) ? null : DateParser.parseDDMMYYYY(birthDate);
        return service.search(at, firstName, lastName, gender, birth, afm, address);
    }
}
