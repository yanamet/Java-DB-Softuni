package _04;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Scanner;

public class _04Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("CodeFirst");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();


        //Здравей, опитах се да направя методите за вкарване от конзолата, но излиза грешка
// (entity) is not mapped, ако намериш какво пропускам ще бъда благодарна :)

       // Diagnose diagnose = getOrCreateDiagnose(scanner, entityManager);
      //  Patient patient = getOrCreatePatient(scanner, entityManager);

        Diagnose diagnose = new Diagnose("Diagnose name", "comment");
        Patient patient = new Patient("Patient", "Patient", "address",
                "mail", LocalDate.now(), "url", true);

        Medicament medicament = new Medicament("Medicament");

        Visitation  visitation = new Visitation(patient, diagnose, LocalDate.now(), "comments");
        visitation.addMedicament(medicament);

        entityManager.persist(patient);
        entityManager.persist(medicament);
        entityManager.persist(visitation);

        entityManager.getTransaction().commit();
        entityManager.close();

    }

    private static Diagnose getOrCreateDiagnose(Scanner scanner, EntityManager entityManager) {
        System.out.println("Would you like to add a new diagnose ? Please type YES or NO");
        String decision = scanner.nextLine();
        Diagnose diagnose;

        if(decision.equals("YES")){
            diagnose = createDiagnose(scanner, entityManager);
        }else{
            diagnose = getDiagnose(scanner, entityManager);
        }
        return diagnose;
    }

    private static Diagnose getDiagnose(Scanner scanner, EntityManager entityManager) {
        Diagnose diagnose;

        System.out.println("Please enter the NAME of the diagnose you are looking for: ");
        String name = scanner.nextLine();

        diagnose = entityManager.createQuery("SELECT d FROM Diagnose d WHERE d.name =: name", Diagnose.class)
                .setParameter("name", name).getSingleResult();

        return diagnose;
    }

    private static Diagnose createDiagnose(Scanner scanner, EntityManager entityManager) {
        System.out.println("Please enter the name of the diagnose");
        String name = scanner.nextLine();

        System.out.println("Please enter the a comment for the diagnose");
        String comment = scanner.nextLine();

        Diagnose diagnose = new Diagnose(name, comment);
        entityManager.persist(diagnose);
        return diagnose;
    }

    private static Patient getOrCreatePatient(Scanner scanner, EntityManager entityManager) {

        System.out.println("Would you like to create a new patient ? Please enter YES or NO");
        String decision = scanner.nextLine();
        Patient patient;

        if(decision.equals("YES")){
            patient = createPatient(scanner, entityManager);
        }else{
            patient = getPatient(scanner, entityManager);
        }

        return patient;
    }

    private static Patient getPatient(Scanner scanner, EntityManager entityManager){
        System.out.println("Please enter the first and last name of the patient");
        String[] name = scanner.nextLine().split(" ");

        Patient patient = entityManager.createQuery("SELECT p FROM Patient p WHERE p.firstName = :fname" +
                " AND p.lastName = :lName", Patient.class)
                .setParameter("firstName", name[0])
                .setParameter("lastName", name[1])
                .getSingleResult();

        entityManager.persist(patient);

        return patient;
    }

    private static Patient createPatient(Scanner scanner, EntityManager entityManager) {
        System.out.println("Please enter patient's first and last name: ");
        String[] patientNames = scanner.nextLine().split(" ");

        System.out.println("Please enter patient's address");
        String address = scanner.nextLine();

        System.out.println("Please enter patient's email");
        String email = scanner.nextLine();

        System.out.println("Please enter patient's pictureUrl");
        String pictureUrl = scanner.nextLine();

        System.out.println("Please enter type YES or NO depending on if the patient has an insurance");
        boolean insurance = scanner.nextLine().equals("YES");

        Patient patient = new Patient(patientNames[0], patientNames[1], address,
                email, LocalDate.now(), pictureUrl, insurance);

        entityManager.persist(patient);

        return patient;
    }
}
