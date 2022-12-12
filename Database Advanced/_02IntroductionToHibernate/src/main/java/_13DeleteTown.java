import entities.Address;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class _13DeleteTown {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String townName = scanner.nextLine();

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("PU_Name");

        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Town town = entityManager.createQuery("SELECT t FROM Town t WHERE t.name = :townName", Town.class)
                .setParameter("townName", townName)
                .getSingleResult();


        List<Address> filteredAddresses = entityManager
                .createQuery("SELECT a FROM Address a", Address.class)
                .getResultList()
                .stream()
                .filter(a -> a.getTown() != null)
                .filter(a -> a.getTown().getName().equals(townName))
                .collect(Collectors.toList());

        String addressInput = filteredAddresses.size() == 1
                ? "1 address in " + townName + " deleted"
                : filteredAddresses.size() + " addresses in " + townName + " deleted";

        System.out.println(addressInput);

        filteredAddresses.forEach(entityManager::remove);

        entityManager.remove(town);

        entityManager.getTransaction().commit();
        entityManager.close();
    }


}
