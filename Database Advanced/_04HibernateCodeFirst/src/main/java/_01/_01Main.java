package _01;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class _01Main {

    private static final String firstName = "Nikolay";
    private static final String lastName = "Nikolaev";
    private static final String notes = "These are my notes. I am a test field.";
    private static final int age = 25;
    private static final String magicWandCreator = "Potter";
    private static final int magicWandSize = 25;
    private static final String depositGroup = "witchers";
    private static final LocalDateTime startTime = LocalDateTime.now();
    private static final float depositAmount = 250;
    private static final float depositInterest = 250;
    private static final float depositCharge = 25;
    private static final LocalDateTime expirationDate = LocalDateTime.now();
    private static final boolean isDepositExpired = true;

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("CodeFirst");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        WizardsDeposits wizardsDeposits = new WizardsDeposits(firstName, lastName, notes, age, magicWandCreator, (short) magicWandSize
                , depositGroup, startTime, depositAmount, depositInterest, depositCharge,
                expirationDate, isDepositExpired);
        entityManager.persist(wizardsDeposits);

        entityManager.getTransaction().commit();
        entityManager.close();

    }
}
