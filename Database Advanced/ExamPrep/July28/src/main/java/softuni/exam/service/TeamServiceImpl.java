package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.domain.entities.dto.ImportRootTeamDTO;
import softuni.exam.domain.entities.dto.ImportTeamDTO;
import softuni.exam.repository.TeamRepository;

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
public class TeamServiceImpl implements TeamService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "teams.xml");

    private final TeamRepository teamRepository;

    private final PictureService pictureService;
    private final Unmarshaller unmarshaller;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public TeamServiceImpl(TeamRepository teamRepository, PictureService pictureService) throws JAXBException {
        this.teamRepository = teamRepository;
        this.pictureService = pictureService;

        JAXBContext context = JAXBContext.newInstance(ImportRootTeamDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public String importTeams() throws FileNotFoundException, JAXBException {

        ImportRootTeamDTO unmarshal = (ImportRootTeamDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return unmarshal
                .getTeams()
                .stream()
                .map(this::importTeam)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    private String importTeam(ImportTeamDTO importTeamDTO){
        Set<ConstraintViolation<ImportTeamDTO>> errors = this.validator.validate(importTeamDTO);

        if(!errors.isEmpty()){
            return "Invalid team";
        }

       Optional<Picture> optPic = this.pictureService.getPicByUrl(importTeamDTO.getPicture().getUrl());

        if(optPic.isEmpty()){
            return "Invalid team";
        }

        Team team = this.modelMapper.map(importTeamDTO, Team.class);
        team.setPicture(optPic.get());

        this.teamRepository.save(team);

        return String.join("Successfully imported - %s", team.getName());

    }

    @Override
    public Optional<Team> getTeamByName(String name) {
        return this.teamRepository.findByName(name);
    }
}
