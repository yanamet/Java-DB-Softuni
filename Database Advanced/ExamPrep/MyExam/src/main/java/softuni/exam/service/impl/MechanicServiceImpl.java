package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportMechanicDTO;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MechanicServiceImpl implements MechanicService {

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "mechanics.json");

    private final MechanicRepository mechanicRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public MechanicServiceImpl(MechanicRepository mechanicRepository, ValidationUtil validationUtil) {
        this.mechanicRepository = mechanicRepository;
        this.validationUtil = validationUtil;

        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importMechanics() throws IOException {
        String json = this.readMechanicsFromFile();
        ImportMechanicDTO[] mechanicDTOS = this.gson.fromJson(json, ImportMechanicDTO[].class);
        return Arrays.stream(mechanicDTOS)
                .map(this::importMechanic)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Mechanic> getMechanicByFirstName(String firstName) {
        return this.mechanicRepository.findByFirstName(firstName);
    }

    private String importMechanic(ImportMechanicDTO importMechanicDTO){

        if(!this.validationUtil.isValid(importMechanicDTO)){
            return "Invalid mechanic";
        }

        Optional<Mechanic> optMechanicEmail = this.mechanicRepository
                .findByEmail(importMechanicDTO.getEmail());

        if(optMechanicEmail.isPresent()){
            return "Invalid mechanic";
        }

        Optional<Mechanic> optMechanicName = this.mechanicRepository
                .findByFirstName(importMechanicDTO.getFirstName());

        if(optMechanicName.isPresent()){
            return "Invalid mechanic";
        }

        if(importMechanicDTO.getPhone() != null) {

            Optional<Mechanic> optMechanicPhone = this.mechanicRepository
                    .findByPhone(importMechanicDTO.getPhone());

            if (optMechanicPhone.isPresent()) {
                return "Invalid mechanic";
            }

        }

        Mechanic mechanic = this.modelMapper.map(importMechanicDTO, Mechanic.class);

        this.mechanicRepository.save(mechanic);

        return String.format("Successfully imported mechanic %s %s",
                mechanic.getFirstName(), mechanic.getLastName());
    }

}
