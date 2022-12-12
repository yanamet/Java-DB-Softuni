package hiberspring.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.ImportEmployeeCardDTO;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {

    private final EmployeeCardRepository employeeCardRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, ValidationUtil validationUtil) {
        this.employeeCardRepository = employeeCardRepository;
        this.validationUtil = validationUtil;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.modelMapper = new ModelMapper();
    }


    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return String.join("\n", Files.readAllLines(
                Path.of(Constants.PATH_TO_FILES + "employee-cards.json")));
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) {

        ImportEmployeeCardDTO[] employeeCardDTOS =
                this.gson.fromJson(employeeCardsFileContent, ImportEmployeeCardDTO[].class);

        return Arrays.stream(employeeCardDTOS)
                .map(this::importEmployeeCard)
                .collect(Collectors.joining("\n"));
    }

    private String importEmployeeCard(ImportEmployeeCardDTO importEmployeeCardDTO){

        if(!this.validationUtil.isValid(importEmployeeCardDTO)){
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Optional<EmployeeCard> optCard = this.employeeCardRepository
                .findByNumber(importEmployeeCardDTO.getNumber());

        if(optCard.isPresent()){
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        EmployeeCard employeeCard = this.modelMapper.map(importEmployeeCardDTO, EmployeeCard.class);

        this.employeeCardRepository.save(employeeCard);

        return String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, "Employee Card", employeeCard.getNumber());
    }

    @Override
    public Optional<EmployeeCard> getCardByNumber(String number) {
        return this.employeeCardRepository.findByNumber(number);
    }
}
