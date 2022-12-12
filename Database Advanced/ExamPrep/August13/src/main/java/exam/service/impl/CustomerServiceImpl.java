package exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exam.model.dto.ImportCustomerDTO;
import exam.model.entity.Customer;
import exam.model.entity.Town;
import exam.repository.CustomerRepository;
import exam.service.CustomerService;
import exam.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "customers.json");
    private final CustomerRepository customerRepository;
    private final TownService townService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public CustomerServiceImpl(CustomerRepository customerRepository, TownService townService) {
        this.customerRepository = customerRepository;
        this.townService = townService;

        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();

        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importCustomers() throws IOException {
        String json = this.readCustomersFileContent();
        ImportCustomerDTO[] customerDTOS = this.gson.fromJson(json, ImportCustomerDTO[].class);

        return Arrays.stream(customerDTOS)
                .map(this::importCustomer)
                .collect(Collectors.joining("\n"));

    }

    private String importCustomer(ImportCustomerDTO importCustomerDTO){

        Set<ConstraintViolation<ImportCustomerDTO>> errors = this.validator.validate(importCustomerDTO);

        if(!errors.isEmpty()){
            return "Invalid Customer";
        }

      Optional<Customer> optCustmoer = this.customerRepository.findByEmail(importCustomerDTO.getEmail());

        if(optCustmoer.isPresent()){
            return "Invalid Customer";
        }

        Town town = this.townService.getTownByName(importCustomerDTO.getTown().getName());
        Customer customer = this.modelMapper.map(importCustomerDTO, Customer.class);
        customer.setTown(town);
        this.customerRepository.save(customer);

        return String.format("Successfully imported Customer %s %s - %s",
                customer.getFirstName(), customer.getLastName(), customer.getEmail());
    }

}
