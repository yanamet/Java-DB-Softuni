package hiberspring.service.impl;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.ImportEmployeeDTO;
import hiberspring.domain.dtos.ImportEmployeeRootDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.BranchService;
import hiberspring.service.EmployeeCardService;
import hiberspring.service.EmployeeService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchService branchService;
    private final EmployeeCardService employeeCardService;
    private final ValidationUtil validationUtil;
    private final Unmarshaller unmarshal;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               BranchService branchService, EmployeeCardService employeeCardService,
                               ValidationUtil validationUtil) throws JAXBException {
        this.employeeRepository = employeeRepository;
        this.branchService = branchService;
        this.employeeCardService = employeeCardService;

        this.validationUtil = validationUtil;

        JAXBContext context = JAXBContext.newInstance(ImportEmployeeRootDTO.class);
        this.unmarshal = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();

    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return String.join("\n",
                Files.readAllLines(Path.of(Constants.PATH_TO_FILES + "employees.xml")));
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {

        ImportEmployeeRootDTO employeeRoot = (ImportEmployeeRootDTO) this.unmarshal
                .unmarshal(new FileReader(Constants.PATH_TO_FILES + "employees.xml"));

        return employeeRoot
                .getEmployees()
                .stream()
                .map(this::importEmployee)
                .collect(Collectors.joining("\n"));
    }

    private String importEmployee(ImportEmployeeDTO importEmployeeDTO) {

        if (!validationUtil.isValid(importEmployeeDTO)) {
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Optional<EmployeeCard> optCard = this.employeeCardService
                .getCardByNumber(importEmployeeDTO.getCard());

        if (optCard.isEmpty()) {
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Optional<Branch> optBranch = this.branchService.getBranchByName(importEmployeeDTO.getBranch());

        if (optBranch.isEmpty()) {
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Optional<Employee> optEmployee = this.employeeRepository.findByCard(optCard.get());

        if (optEmployee.isPresent()) {
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Employee employee = this.modelMapper.map(importEmployeeDTO, Employee.class);

        employee.setCard(optCard.get());
        employee.setBranch(optBranch.get());

        this.employeeRepository.save(employee);

        return String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                "Employee", employee.getFirstName() + " " + employee.getLastName());

    }

    @Override
    public String exportProductiveEmployees() {
        List<Employee> mostProductiveEmployees = this.employeeRepository.findTheMostProductiveEmployees();
        return mostProductiveEmployees
                .stream()
                .map(Employee::toString)
                .collect(Collectors.joining("--------------------------\n"));
    }
}
