package _02;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class _02Main {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("CodeFirst");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Product product = new Product("Strawberry", 1.0, BigDecimal.valueOf(2.5));
        Customer customer = new Customer("Customer", "@mail", "1234");
        StoreLocation storeLocation = new StoreLocation("location");
        Sale sale = new Sale(product, customer, storeLocation);

        entityManager.persist(product);
        entityManager.persist(customer);
        entityManager.persist(storeLocation);
        entityManager.persist(sale);

        entityManager.getTransaction().commit();
        entityManager.close();

    }
}
