package gr.registry.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Οντότητα Πολίτη.
 * - AT: 8 χαρακτήρες, μοναδικό (ελέγχεται στο service).
 * - Όνομα, Επίθετο, Φύλο, Ημερομηνία Γέννησης: Υποχρεωτικά.
 * - ΑΦΜ: προαιρετικό, 9 ψηφία.
 * - Διεύθυνση: προαιρετικό.
 */
@Entity
@Table(name = "citizens", indexes = {
        @Index(name = "uk_citizen_at", columnList = "at", unique = true)
})
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Αριθμός Ταυτότητας (AT) – 8 χαρακτήρες. */
    @NotBlank(message = "Ο ΑΤ είναι υποχρεωτικός")
    @Pattern(regexp = "^[A-Za-z0-9]{8}$", message = "Ο ΑΤ πρέπει να αποτελείται από ακριβώς 8 αλφαριθμητικούς χαρακτήρες")
    @Column(name = "at", nullable = false, unique = true, length = 8)
    private String at;

    @NotBlank(message = "Το όνομα είναι υποχρεωτικό")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Το επίθετο είναι υποχρεωτικό")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Το φύλο είναι υποχρεωτικό")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @NotNull(message = "Η ημερομηνία γέννησης είναι υποχρεωτική")
    @Column(nullable = false)
    private LocalDate birthDate;

    @Pattern(regexp = "^(|\\d{9})$", message = "Ο ΑΦΜ (αν δοθεί) πρέπει να έχει 9 ψηφία")
    @Column(length = 9)
    private String afm;

    /** Διεύθυνση κατοικίας (προαιρετική). */
    private String address;

    public Citizen() {}

    public Citizen(String at, String firstName, String lastName, Gender gender, LocalDate birthDate, String afm, String address) {
        this.at = at;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.afm = afm;
        this.address = address;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getAt() { return at; }
    public void setAt(String at) { this.at = at; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getAfm() { return afm; }
    public void setAfm(String afm) { this.afm = afm; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
