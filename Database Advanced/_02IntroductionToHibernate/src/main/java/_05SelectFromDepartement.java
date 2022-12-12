import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class _05SelectFromDepartement {

    static final String selectQuery = "SELECT e FROM Employee e WHERE department_id = 6\n" +
            "ORDER BY salary, employee_id";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Query query = entityManager
                .createQuery(selectQuery, Employee.class);
        List<Employee> resultList = query.getResultList();

        for (Employee employee : resultList) {
            System.out.printf("%s %s from %s - $%.2f%n",
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getDepartment().getName(),
                    employee.getSalary());
        }

        entityManager.getTransaction().commit();
    }
}
