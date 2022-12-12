import entities.Department;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.*;

public class _12MaxSalary {
    public static void main(String[] args) {

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        List<Department> departments = entityManager.createQuery
                ("SELECT d FROM Department d", Department.class)
                .getResultList();

        for (Department department : departments) {

            double maxSalary = department.getEmployees()
                    .stream()
                    .map(Employee::getSalary)
                    .max(BigDecimal::compareTo)
                    .get().doubleValue();

            if(maxSalary < 30000 || maxSalary > 70000){
                System.out.printf("%s %.2f%n", department.getName(), maxSalary);
            }
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }


}
