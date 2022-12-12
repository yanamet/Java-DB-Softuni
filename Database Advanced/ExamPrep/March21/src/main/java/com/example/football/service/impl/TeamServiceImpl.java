package com.example.football.service.impl;

import com.example.football.models.dto.ImportTeamDTO;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

//ToDo - Implement all methods
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelmapper;
    private final Validator validator;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TownRepository townRepository) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.gson = new GsonBuilder().create();
        this.modelmapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "teams.json");
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importTeams() throws IOException {
        String json = this.readTeamsFileContent();
        ImportTeamDTO[] importTeamDTOS = this.gson.fromJson(json, ImportTeamDTO[].class);
        return Arrays.stream(importTeamDTOS).map(this::importTeam)
                .collect(Collectors.joining("\n"));

    }

    private String importTeam(ImportTeamDTO importTeamDTO) {
        Set<ConstraintViolation<ImportTeamDTO>> validateErrors =
                this.validator.validate(importTeamDTO);

        if (!validateErrors.isEmpty()) {
            return "Invalid Team";
        }

        Optional<Team> optTeam = this.teamRepository.findByName(importTeamDTO.getName());

        if (optTeam.isPresent()) {
            return "Invalid Team";
        }

        Team team = this.modelmapper.map(importTeamDTO, Team.class);
        Town town = this.townRepository.findByName(importTeamDTO.getTownName()).get();
        team.setTown(town);
        this.teamRepository.save(team);

        return String.format("Successfully imported Team %s - %d",
                team.getName(), team.getFanBase());

    }

}
