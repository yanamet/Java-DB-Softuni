package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportTownDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;

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

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "towns.json");


    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public TownServiceImpl(TownRepository townRepository) {
        this.townRepository = townRepository;

        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
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
        ImportTownDTO[] townsDTO = this.gson.fromJson(json, ImportTownDTO[].class);
        return Arrays.stream(townsDTO)
                .map(this::importTown)
                .collect(Collectors.joining("\n"));

    }

    @Override
    public Town getTownByName(String town) {
        return this.townRepository.findByTownName(town).get();
    }

    @Override
    public Optional<Town> getOptionalTownByName(String town) {
        return this.townRepository.findByTownName(town);
    }

    private String importTown(ImportTownDTO townDTO) {
        Set<ConstraintViolation<ImportTownDTO>> errors = this.validator.validate(townDTO);

        if (!errors.isEmpty()) {
            return "Invalid town";
        }

        Optional<Town> optTown = this.townRepository
                .findByTownName(townDTO.getTownName());

        if (optTown.isPresent()) {
            return "Invalid town";
        }

        Town town = this.modelMapper.map(townDTO, Town.class);

        this.townRepository.save(town);

        return String.format("Successfully imported town %s - %d",
                town.getTownName(), town.getPopulation());

    }

}
