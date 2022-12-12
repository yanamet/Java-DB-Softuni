import entities.Town;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Query q = entityManager.createQuery("SELECT t FROM Town t " +
                "WHERE CHAR_LENGTH(t.name) <= 5", Town.class);
        List<Town> resultList = q.getResultList();

        for (Town town : resultList) {
            String oldName = town.getName();
            town.setName(oldName.toUpperCase());

            entityManager.persist(town);
        }

        for (Town town : resultList) {
            System.out.println(town);;
        }


        entityManager.getTransaction().commit();

    }
}
