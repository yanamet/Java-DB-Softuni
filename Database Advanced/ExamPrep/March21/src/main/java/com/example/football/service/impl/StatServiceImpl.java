package com.example.football.service.impl;

import com.example.football.models.dto.ImportSingleStatDTO;
import com.example.football.models.dto.ImportStatsDTO;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {

    private final Path path = Path.of
            ("src", "main", "resources", "files", "xml", "stats.xml");

    private final StatRepository statRepository;
    private final Unmarshaller unmarshaller;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public StatServiceImpl(StatRepository statRepository) throws JAXBException {
        this.statRepository = statRepository;
        JAXBContext context = JAXBContext.newInstance(ImportStatsDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper = new ModelMapper();

    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importStats() throws FileNotFoundException, JAXBException {
        ImportStatsDTO statsDTO = (ImportStatsDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return statsDTO
                .getStats()
                .stream()
                .map(this::importStat)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importStat(ImportSingleStatDTO importStatDTO){
        Set<ConstraintViolation<ImportSingleStatDTO>> validateErrors =
                this.validator.validate(importStatDTO);

        if(!validateErrors.isEmpty()){
            return "Invalid Stat";
        }

        Optional<Stat> optStat = this.statRepository.findByPassingAndShootingAndEndurance
                (importStatDTO.getPassing(), importStatDTO.getShooting(), importStatDTO.getEndurance());

        if(optStat.isPresent()){
            return "Invalid Stat";
        }

        Stat stat = this.modelMapper.map(importStatDTO, Stat.class);
        this.statRepository.save(stat);
        return String.format("Successfully imported Stat %.2f - %.2f - %.2f",
                stat.getPassing(),stat.getShooting(), stat.getEndurance());

    }

}
