import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class _11FindByFirstName {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String startingLetters = scanner.nextLine();

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e ", Employee.class)
                .getResultList();

       List<Employee> filtered = employees.stream()
               .filter(e -> e.getFirstName().toLowerCase().startsWith(startingLetters.toLowerCase()))
               .collect(Collectors.toList());


        for (Employee employee : filtered) {
            System.out.printf("%s %s - %s - ($%.2f)%n",
                    employee.getFirstName(), employee.getLastName(),
                    employee.getJobTitle(), employee.getSalary());
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
