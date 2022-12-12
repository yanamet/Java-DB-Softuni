package hiberspring.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.ImportTownDTO;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return String.join("\n",
                Files.readAllLines(Path.of(Constants.PATH_TO_FILES + "towns.json")));
    }

    @Override
    public String importTowns(String townsFileContent) {
        ImportTownDTO[] townDTOS = this.gson.fromJson(townsFileContent, ImportTownDTO[].class);

        return Arrays.stream(townDTOS)
                .map(this::importTown)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Town> getTownByName(String name) {
        return this.townRepository.findByName(name);
    }

    private String importTown(ImportTownDTO importTownDTO){

        Set<ConstraintViolation<ImportTownDTO>> errors = this.validator.validate(importTownDTO);
        if(!errors.isEmpty() || importTownDTO.getPopulation() == 0){
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Optional<Town> optTown = this.townRepository.findByName(importTownDTO.getName());

        if(optTown.isPresent()){
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Town town = this.modelMapper.map(importTownDTO, Town.class);
        this.townRepository.save(town);

        return String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, "Town", town.getName());

    }

}
