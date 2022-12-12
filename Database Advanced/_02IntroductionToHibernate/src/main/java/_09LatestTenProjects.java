import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

public class _09LatestTenProjects {
    public static void main(String[] args) {

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        List<Project> resultList = entityManager.createQuery
                ("SELECT p FROM Project p " +
                        " ORDER BY p.startDate DESC", Project.class)
                .setMaxResults(10)
                .getResultList();

        resultList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

        for (Project project : resultList) {
            System.out.println(project); //toString метод в Project класа
        }


        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
