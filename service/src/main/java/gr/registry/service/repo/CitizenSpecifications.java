package gr.registry.service.repo;

import gr.registry.domain.Citizen;
import gr.registry.domain.Gender;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CitizenSpecifications {
    public static Specification<Citizen> byFilters(
            String at, String firstName, String lastName,
            Gender gender, LocalDate birthDate, String afm, String address) {

        return Specification.where(eq("at", at))
                .and(like("firstName", firstName))
                .and(like("lastName", lastName))
                .and(eq("gender", gender))
                .and(eq("birthDate", birthDate))
                .and(eq("afm", afm))
                .and(like("address", address));
    }

    private static <T> Specification<Citizen> eq(String field, T value) {
        return (root, query, cb) -> value == null ? null : cb.equal(root.get(field), value);
    }

    private static Specification<Citizen> like(String field, String value) {
        return (root, query, cb) -> (value == null || value.isBlank())
                ? null
                : cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }
}
