package gr.registry.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.util.Scanner;

/**
 * Απλός CLI πελάτης με μενού. Τερματίζει σε μη έγκυρη επιλογή (σύμφωνα με την εκφώνηση).
 */
@SpringBootApplication
public class RegistryClientApplication implements CommandLineRunner {

    private final WebClient http = WebClient.create("http://localhost:8080");

    public static void main(String[] args) {
        SpringApplication.run(RegistryClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("=== Μητρώο Πολιτών (Client) ===");
            System.out.println("1) Εισαγωγή");
            System.out.println("2) Διαγραφή");
            System.out.println("3) Ενημέρωση (ΑΦΜ/Διεύθυνση)");
            System.out.println("4) Αναζήτηση");
            System.out.println("5) Εμφάνιση (με ΑΤ)");
            System.out.print("Επιλογή: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> create(sc);
                    case "2" -> delete(sc);
                    case "3" -> update(sc);
                    case "4" -> search(sc);
                    case "5" -> get(sc);
                    default -> { System.out.println("Μη έγκυρη επιλογή. Τερματισμός."); return; }
                }
            } catch (Exception ex) {
                System.out.println("Σφάλμα: " + ex.getMessage());
            }
        }
    }

    void create(Scanner sc) {
        System.out.print("AT (8 chars): "); String at = sc.nextLine();
        System.out.print("Όνομα: "); String fn = sc.nextLine();
        System.out.print("Επίθετο: "); String ln = sc.nextLine();
        System.out.print("Φύλο (MALE/FEMALE/OTHER): "); String g = sc.nextLine();
        System.out.print("Ημ/νία γέννησης (DD-MM-YYYY): "); String bd = sc.nextLine();
        System.out.print("ΑΦΜ (9 ψηφία ή κενό): "); String afm = sc.nextLine();
        System.out.print("Διεύθυνση (προαιρετική): "); String address = sc.nextLine();

        String json = String.format(
        	    "{\"at\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"gender\":\"%s\",\"birthDate\":\"%s\",\"afm\":\"%s\",\"address\":\"%s\"}",
        	    at, fn, ln, g, bd, afm, address
        	);


        String resp = http.post().uri("/citizens")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(resp);
    }

    void delete(Scanner sc) {
        System.out.print("AT: "); String at = sc.nextLine();
        http.delete().uri("/citizens/" + at).retrieve().toBodilessEntity().block();
        System.out.println("Διαγράφηκε (αν υπήρχε).");
    }

    void update(Scanner sc) {
        System.out.print("AT: "); String at = sc.nextLine();
        System.out.print("Νέο ΑΦΜ (ή κενό): "); String afm = sc.nextLine();
        System.out.print("Νέα Διεύθυνση (ή κενό): "); String address = sc.nextLine();
        String json = String.format("{\"afm\":\"%s\",\"address\":\"%s\"}", afm, address);
        String resp = http.patch().uri("/citizens/" + at)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(resp);
    }

    void search(Scanner sc) {
        System.out.print("Πεδίο αναζήτησης (π.χ. lastName=Pap): ");
        String q = sc.nextLine();
        String resp = http.get().uri("/citizens/search?" + q)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(resp);
    }

    void get(Scanner sc) {
        System.out.print("AT: "); String at = sc.nextLine();
        String resp = http.get().uri("/citizens/" + at)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(resp);
    }
}
