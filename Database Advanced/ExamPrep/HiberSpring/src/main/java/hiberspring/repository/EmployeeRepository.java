package hiberspring.repository;

import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCard(EmployeeCard employeeCard);

    //•	Order the data by full name in alphabetical order, and then by length of position in descending order.

    @Query("SELECT e FROM Employee e WHERE SIZE(e.branch.products) > 0 " +
            " ORDER BY concat(e.firstName, ' ', e.lastName) ASC, " +
            "length(e.position) desc")
    List<Employee> findTheMostProductiveEmployees();

    // TODO: Implement me
}
