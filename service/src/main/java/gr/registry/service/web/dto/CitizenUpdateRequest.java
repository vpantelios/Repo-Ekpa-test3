package gr.registry.service.web.dto;

import jakarta.validation.constraints.Pattern;

/** Ενημέρωση μόνο για ΑΦΜ & Διεύθυνση. */
public class CitizenUpdateRequest {
    @Pattern(regexp = "^(|\\d{9})$", message = "Ο ΑΦΜ (αν δοθεί) πρέπει να έχει 9 ψηφία")
    private String afm;
    private String address;

    public String getAfm() { return afm; }
    public void setAfm(String afm) { this.afm = afm; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
