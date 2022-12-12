package com.example.football.service.impl;

import com.example.football.models.dto.ImportPlayersDTO;
import com.example.football.models.dto.ImportSinglePlayerDTO;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final Path path = Path.of
            ("src", "main", "resources", "files", "xml", "players.xml");
    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;
    private final TeamRepository teamRepository;
    private final StatRepository statRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;
//    private final TypeMap<ImportSinglePlayerDTO, Player> dtoToPlayer;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TownRepository townRepository,
                             TeamRepository teamRepository, StatRepository statRepository) throws JAXBException {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.teamRepository = teamRepository;
        this.statRepository = statRepository;
        JAXBContext context = JAXBContext.newInstance(ImportPlayersDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper = new ModelMapper();

//        Converter<String, LocalDate> dateConverter = s -> s.getSource() == null ? null
//                : LocalDate.parse(s.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);

//        TypeMap<ImportSinglePlayerDTO, Player> typeMap = this.modelMapper
//                .createTypeMap(ImportSinglePlayerDTO.class, Player.class);
//
//        this.dtoToPlayer = typeMap.addMappings(map ->
//                map.using(dateConverter).map(ImportSinglePlayerDTO::getBirthDate, Player::setBirthdate));


    }


    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importPlayers() throws FileNotFoundException, JAXBException {
        ImportPlayersDTO playersDTO = (ImportPlayersDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return playersDTO
                .getPlayers()
                .stream()
                .map(this::importPlayer)
                .collect(Collectors.joining("\n"));


    }

    @Override
    public String exportBestPlayers() {

        LocalDate afterDate = LocalDate.of(1995, 1, 1);
        LocalDate beforeDate = LocalDate.of(2003, 1, 1);

        List<Player> players = this.playerRepository
                .findByBirthdateBetweenOrderByStatShootingDescStatPassingDescStatEnduranceDescLastName
                        (afterDate, beforeDate);

        return players
                .stream()
                .map(Player::toString)
                .collect(Collectors.joining("\n"));
    }

    private String importPlayer(ImportSinglePlayerDTO dto) {
        Set<ConstraintViolation<ImportSinglePlayerDTO>> validateErrors =
                this.validator.validate(dto);

        if(!validateErrors.isEmpty()){
            return "Invalid Player";
        }

        Optional<Player> optPlayer = this.playerRepository
                .findByEmail(dto.getEmail());

        if(optPlayer.isPresent()){
            return "Invalid Player";
        }

        Town town = this.townRepository.findByName(dto.getTown().getName()).get();
        Team team = this.teamRepository.findByName(dto.getTeam().getName()).get();
        Stat stat = this.statRepository.findById(dto.getStat().getId()).get();

        Player player = this.modelMapper.map(dto, Player.class);

        player.setTown(town);
        player.setTeam(team);
        player.setStat(stat);

        this.playerRepository.save(player);

        return String.format("Successfully imported Player %s %s - %s", player.getFirstName(),
                player.getLastName(), player.getPosition().toString());
    }

}
