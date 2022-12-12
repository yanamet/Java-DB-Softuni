import entities.Employee;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class _04EmployeesWithSalaryOver {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Query query = entityManager
                .createQuery("SELECT e FROM Employee e WHERE salary > 50000", Employee.class);
        List<Employee> resultList = query.getResultList();

        for (Employee employee : resultList) {
            System.out.println(employee.getFirstName());
        }

        entityManager.getTransaction().commit();
    }
}
