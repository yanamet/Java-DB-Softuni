package _05;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class _05Main {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CodeFirst");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        User user = new User("user", "user", "mail", "pass");
        BillingDetails creditCard = new CreditCard("3456", user, "visa", LocalDate.now(), LocalDate.now());

        user.addBillingDetail(creditCard);
        entityManager.persist(user);
        entityManager.persist(creditCard);


        entityManager.getTransaction().commit();
        entityManager.close();

    }
}
