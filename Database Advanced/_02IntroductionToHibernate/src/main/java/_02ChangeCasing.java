import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class _02ChangeCasing {
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


        entityManager.getTransaction().commit();

    }
}
