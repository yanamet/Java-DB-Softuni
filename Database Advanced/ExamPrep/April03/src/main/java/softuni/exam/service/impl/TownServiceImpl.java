package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportTownDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "towns.json");
    private final Gson gson;

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    private final TownRepository townRepository;

    @Autowired
    public TownServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;

        this.gson = new GsonBuilder().create();

    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importTowns() throws IOException {
        String json = this.readTownsFileContent();

        ImportTownDTO[] townDTOS = this.gson.fromJson(json, ImportTownDTO[].class);

        return Arrays.stream(townDTOS)
                .map(this::importTown)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Town> getTownByName(String name) {
        return this.townRepository.findByName(name);
    }

    private String importTown(ImportTownDTO importTownDTO){

        if(!this.validationUtil.isValid(importTownDTO)){
            return "Invalid Town";
        }

        Optional<Town> optionalTown = this.townRepository.findByName(importTownDTO.getName());

        if(optionalTown.isPresent()){
            return "Invalid Town";
        }

        Town town = this.modelMapper.map(importTownDTO, Town.class);
        this.townRepository.save(town);

        return String.format("Successfully imported Town %s - %d",
                town.getName(), town.getPopulation());
    }

}
