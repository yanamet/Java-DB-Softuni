package exam.service.impl;

import exam.model.dto.ImportRootTownDTO;
import exam.model.dto.ImportTownDTO;
import exam.model.entity.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {

    private final Unmarshaller unmarshaller;
    private final ModelMapper modelMapper;
    private final Validator validator;
    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "towns.xml");
    private final TownRepository townRepository;

    public TownServiceImpl(TownRepository townRepository) throws JAXBException {
        this.townRepository = townRepository;

        JAXBContext context = JAXBContext.newInstance(ImportRootTownDTO.class);
        this.unmarshaller = context.createUnmarshaller();
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
    public String importTowns() throws JAXBException, FileNotFoundException {

        ImportRootTownDTO unmarshal = (ImportRootTownDTO) unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return unmarshal.getTowns().stream()
                .map(this::importTown)
                .collect(Collectors.joining("\n"));

    }

    @Override
    public Town getTownByName(String name) {
        return this.townRepository.findByName(name).get();
    }

    private String importTown(ImportTownDTO importTownDTO){
        Set<ConstraintViolation<ImportTownDTO>> errors = this.validator.validate(importTownDTO);

        if(!errors.isEmpty()){
            return "Invalid Town";
        }

       Optional<Town> optTown = this.townRepository.findByName(importTownDTO.getName());

        if(optTown.isPresent()){
            return "Invalid Town";
        }

        Town town = this.modelMapper.map(importTownDTO, Town.class);
        this.townRepository.save(town);

        return String.format("Successfully imported Town %s", town.getName());

    }

}
