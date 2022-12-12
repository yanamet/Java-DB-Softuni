import entities.Department;
import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class _10IncreaseSalaries {

    public static void main(String[] args) {

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        List<Employee> resultList = entityManager.createQuery
                ("SELECT e FROM Employee e ", Employee.class)
                .getResultList();

         List<Employee> newList = resultList
                .stream()
                .filter(e -> e.getDepartment().getName().equals("Engineering")
                || e.getDepartment().getName().equals("Tool Design")
                ||e.getDepartment().getName().equals("Marketing")
                ||e.getDepartment().getName().equals("Information Services"))
         .collect(Collectors.toList());

        for (Employee employee : newList) {
            employee.setSalary(employee.getSalary().multiply(BigDecimal.valueOf(1.12)));
        }

        for (Employee employee : newList) {
            System.out.println(employee);
        }


        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
