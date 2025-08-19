package gr.registry.service.web.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Βοηθητική κλάση για μετατροπή DD-MM-YYYY -> LocalDate. */
public final class DateParser {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("dd-MM-uuuu");
    private DateParser() {}
    public static LocalDate parseDDMMYYYY(String ddMMyyyy) {
        try {
            return LocalDate.parse(ddMMyyyy, F);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Μη έγκυρη ημερομηνία (αναμενόταν DD-MM-YYYY)");
        }
    }
}
