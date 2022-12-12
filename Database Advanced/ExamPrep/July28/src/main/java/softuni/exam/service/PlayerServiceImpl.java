package softuni.exam.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Position;
import softuni.exam.domain.entities.Team;
import softuni.exam.domain.entities.dto.ImportPlayerDTO;
import softuni.exam.repository.PlayerRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final List<Position> VALID_POSITIONS = Arrays.asList(Position.ST, Position.RB, Position.CM,
            Position.RM, Position.LM, Position.LW, Position.RW
    ) ;

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "players.json");

    private final ModelMapper modelMapper;
    private final PictureService pictureService;
    private final TeamService teamService;
    private final Gson gson;
    private final Validator validator;

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PictureService pictureService, TeamService teamService, PlayerRepository playerRepository) {
        this.pictureService = pictureService;
        this.teamService = teamService;
        this.playerRepository = playerRepository;

        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }


    @Override
    public String importPlayers() throws IOException {
        String json = this.readPlayersJsonFile();
        ImportPlayerDTO[] playerDTOS = this.gson.fromJson(json, ImportPlayerDTO[].class);
        return Arrays.stream(playerDTOS)
                .map(this::importPlayer)
                .collect(Collectors.joining("\n"));
    }

    private String importPlayer(ImportPlayerDTO importPlayerDTO){

        Set<ConstraintViolation<ImportPlayerDTO>> errors = this.validator.validate(importPlayerDTO);

        if(!errors.isEmpty()){
            return "Invalid player";
        }


        //    "picture": {
        //      "url": "google.pictures#1"
        //    },
        //    "team": {
        //      "name": "West Valley",
        //      "picture": {
        //        "url": "fc_pictures_1"
        //      }
        //    }
        //  },

        if(!VALID_POSITIONS.contains(importPlayerDTO.getPosition())){
            return "Invalid player";
        }

        Optional<Picture> picByUrl = this.pictureService.getPicByUrl(importPlayerDTO.getPicture().getUrl());
        if(picByUrl.isEmpty()){
            return "Invalid player";
        }
        Picture playerPic = picByUrl.get();

        Optional<Team> optTeam = this.teamService.getTeamByName(importPlayerDTO.getTeam().getName());
        if(optTeam.isEmpty()){
            return "Invalid player";
        }
        Team team = optTeam.get();

        Optional<Picture> optTeamPic = this.pictureService
                .getPicByUrl(importPlayerDTO.getTeam().getPicture().getUrl());

        if(optTeamPic.isEmpty()){
            return "Invalid player";
        }

        team.setPicture(optTeamPic.get());

        Player player = this.modelMapper.map(importPlayerDTO, Player.class);
        player.setPicture(playerPic);
        player.setTeam(team);

        this.playerRepository.save(player);

        return String.format("Successfully imported player: %s %s",
                player.getFirstName(), player.getLastName());
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {

        BigDecimal value = BigDecimal.valueOf(100000);

        return this.playerRepository.findAllBySalaryGreaterThan(value)
                .stream()
                .map(Player::playerWithSalaryToString)
                .collect(Collectors.joining("\n"));

    }

    @Override
    public String exportPlayersInATeam() {
        Team team = this.teamService.getTeamByName("North Hub").get();

        return "Team: North Hub\n" +
                this.playerRepository.findAllByTeamOrderById(team)
                .stream()
                .map(Player::playerTeamToString)
                .collect(Collectors.joining("\n"));
    }
}
