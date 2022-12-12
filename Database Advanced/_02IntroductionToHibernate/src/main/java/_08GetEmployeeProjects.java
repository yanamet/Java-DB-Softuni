import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

public class _08GetEmployeeProjects {

    private static final String selectQuery = "SELECT e FROM Employee e WHERE id = :id";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int emplId = Integer.parseInt(scanner.nextLine());

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        Employee employee = entityManager.createQuery(selectQuery, Employee.class)
                .setParameter("id", emplId)
                .getSingleResult();

        System.out.printf("%s %s - %s%n",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getJobTitle());

        printSortedProjects(employee);

        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static void printSortedProjects(Employee employee) {
        Set<Project> projects = employee.getProjects();

        List<String> prList = new ArrayList<>();

        for (Project project : projects) {
            prList.add(project.getName());
        }

        Collections.sort(prList);

        for (String projectName : prList) {
            System.out.println(projectName);
        }

    }

}
