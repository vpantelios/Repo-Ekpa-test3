package gr.registry.service.web.dto;

import jakarta.validation.constraints.*;
import gr.registry.domain.Gender;

/** Αίτημα δημιουργίας πολίτη. Η birthDate δίνεται σε μορφή DD-MM-YYYY. */
public class CitizenCreateRequest {
    @NotBlank @Pattern(regexp = "^[A-Za-z0-9]{8}$")
    private String at;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotNull  private Gender gender;
    /** Αναμενόμενη μορφή: DD-MM-YYYY */
    @NotBlank @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$")
    private String birthDate;
    @Pattern(regexp = "^(|\\d{9})$")
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
