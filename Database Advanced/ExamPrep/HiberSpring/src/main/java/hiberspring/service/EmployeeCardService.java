package hiberspring.service;

import hiberspring.domain.entities.EmployeeCard;

import java.io.IOException;
import java.util.Optional;

public interface EmployeeCardService {

    Boolean employeeCardsAreImported();

    String readEmployeeCardsJsonFile() throws IOException;

    String importEmployeeCards(String employeeCardsFileContent);

    Optional<EmployeeCard> getCardByNumber(String card);
}
