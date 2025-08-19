package gr.registry.service.web.dto;

import jakarta.validation.constraints.*;
import gr.registry.domain.Gender;

/** Αίτημα δημιουργίας πολίτη. Η birthDate δίνεται σε μορφή DD-MM-YYYY. */
public class CitizenCreateRequest {

    @NotBlank(message = "Το AT είναι υποχρεωτικό")
    @Pattern(
    		regexp = "^[A-Z]{2}\\d{6}$",
    		message = "Μη έγκυρο AT (2 κεφαλαία γράμματα + 6 ψηφία, π.χ. AT123456)"
    )
    private String at;

    @NotBlank(message = "Το μικρό όνομα είναι υποχρεωτικό")
    private String firstName;

    @NotBlank(message = "Το επώνυμο είναι υποχρεωτικό")
    private String lastName;

    @NotNull(message = "Το φύλο είναι υποχρεωτικό")
    private Gender gender;

    /** Αναμενόμενη μορφή: DD-MM-YYYY */
    @NotBlank(message = "Η ημερομηνία γέννησης είναι υποχρεωτική (μορφή DD-MM-YYYY)")
    @Pattern(
        regexp = "^\\d{2}-\\d{2}-\\d{4}$",
        message = "Μη έγκυρη ημερομηνία γέννησης (αναμένεται μορφή DD-MM-YYYY)"
    )
    private String birthDate;

    @Pattern(
        regexp = "^(|\\d{9})$",
        message = "Το ΑΦΜ πρέπει να έχει 9 ψηφία (ή αφήστε το κενό)"
    )
    private String afm;

    private String address;

    // getters/setters
    public String getAt() { return at; }
    public void setAt(String at) { this.at = at; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getAfm() { return afm; }
    public void setAfm(String afm) { this.afm = afm; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
