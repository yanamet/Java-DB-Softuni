import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class _03ContainsEmployee {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] employeeInfo = scanner.nextLine().split(" ");


        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Long count = entityManager.createQuery("SELECT COUNT(e) FROM Employee e " +
                "WHERE first_name = :first_name " +
                "AND last_name = :last_name", Long.class)
                .setParameter("first_name", employeeInfo[0])
                .setParameter("last_name", employeeInfo[1])
                .getSingleResult();


        if(count > 0){
            System.out.println("YES");
        }else{
            System.out.println("NO");
        }


        entityManager.getTransaction().commit();

    }
}
