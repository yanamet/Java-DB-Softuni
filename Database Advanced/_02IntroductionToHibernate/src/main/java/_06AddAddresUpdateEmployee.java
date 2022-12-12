import entities.Address;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class _06AddAddresUpdateEmployee {

    static final String addressText = "Vitoshka 15";


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String employeeLastName = scanner.nextLine();

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Address address = new Address();
        address.setText(addressText);
        entityManager.persist(address);

        Employee employee = entityManager.createQuery
                ("SELECT e FROM Employee e WHERE last_name = :last_name", Employee.class)
                .setParameter("last_name", employeeLastName)
                .getSingleResult();

        employee.setAddress(address);
        entityManager.persist(employee);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
