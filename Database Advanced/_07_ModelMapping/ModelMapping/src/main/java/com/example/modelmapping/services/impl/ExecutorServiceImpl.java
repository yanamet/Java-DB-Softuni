package com.example.modelmapping.services.impl;

import com.example.modelmapping.entities.games.Game;
import com.example.modelmapping.entities.games.GameDTO;
import com.example.modelmapping.entities.users.LoginDTO;
import com.example.modelmapping.entities.users.RegistrationDTO;
import com.example.modelmapping.entities.users.User;
import com.example.modelmapping.exceprions.*;
import com.example.modelmapping.services.ExecutorService;
import com.example.modelmapping.services.GameService;
import com.example.modelmapping.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExecutorServiceImpl implements ExecutorService {

    private final UserService userService;
    private GameService gameService;

    @Autowired
    public ExecutorServiceImpl(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public String execute(String commandLine) {


        String[] commands = commandLine.split("\\|");
        String commandName = commands[0];

        String output = switch (commandName) {
            case REGISTER_USER_COMMAND -> registerCase(commands);

            case LOGIN_USER_COMMAND -> loginCase(commands);

            case LOGOUT_USER_COMMAND -> logoutCase();

            case ADD_GAME_COMMAND -> addGameCase(commands);

            case EDIT_GAME_COMMAND -> editGameCase(commands);

            case DELETE_GAME_COMMAND -> deleteCase(Integer.parseInt(commands[1]));

            case ALL_GAMES_COMMAND -> showAllGames();

            case GAME_DETAILS_COMMAND -> showGameDetails(commands[1]);

            default -> "Unknown command.";
        };
        return output;
    }

    private String showGameDetails(String gameTitle) {
        Optional<Game> game = this.gameService.findGameByTitle(gameTitle);

        if(game.isEmpty()){
            throw new NoSuchGameException();
        }

        return game.get().toString();

    }

    private String showAllGames() {
        List<Game> allGames = this.gameService.getAllGames();
        return allGames.stream().map(Game::getTitle).collect(Collectors.joining(System.lineSeparator()));

    }

    private String deleteCase(int gameId) {

        User user = this.userService.getLoggedUser();
        if(!user.isAdministrator()){
            throw new UserIsNotAdministrator();
        }

        Game game = this.gameService.findGameById(gameId);

        if(game == null){
            throw new NoSuchGameException();
        }

        this.gameService.deleteGame(game);

        return game.getTitle() + " is deleted.";

    }

    private String editGameCase(String[] commands) {

        User user = this.userService.getLoggedUser();
        if(!user.isAdministrator()){
            throw new UserIsNotAdministrator();
        }

        int id = Integer.parseInt(commands[1]);

        Game game = this.gameService.findGameById(id);
        if (game == null) {
            throw new NoSuchGameException();
        }

        List<String> collect = Arrays.stream(commands).skip(2).toList();

        for (String command : collect) {
            String[] tokens = command.split("=");
            String column = tokens[0];

            switch (column) {
                case "title":
                    game.setTitle(tokens[1]);
                    break;
                case "price":
                    game.setPrice(BigDecimal.valueOf(Double.parseDouble(tokens[1])));
                    break;
                case "size":
                    game.setSize(Float.parseFloat(tokens[1]));
                    break;
                case "trailer":
                    game.setTrailer(tokens[1]);
                    break;
                case "thubnailURL":
                    game.setImageUrl(tokens[1]);
                    break;
                case "description":
                    game.setDescription(tokens[1]);
                    break;
                case "releaseDate":
                    game.setReleaseDate(LocalDate.parse(tokens[1]));
                    break;
            }

            this.gameService.save(game);
        }
        return game.getTitle() + " is edited.";
    }

    private String addGameCase(String[] commands) {
        User user = this.userService.getLoggedUser();

        if (!user.isAdministrator()) {
            throw new UserIsNotAdministrator();
        }

        ModelMapper mapper = new ModelMapper();
        GameDTO gameData = new GameDTO(commands);

        Optional<Game> added = this.gameService.findByTitle(gameData.getTitle());
        if (added.isPresent()) {
            throw new GameAlreadyAdded();
        }

        Game game = mapper.map(gameData, Game.class);
        this.gameService.addGame(game);
        return String.format("Added %s.", game.getTitle());
    }

    private String logoutCase() {
        User user = this.userService.getLoggedUser();

        if (user == null) {
            throw new NoLoggedInUser();
        }
        this.userService.logout();
        return String.format("%s is successfully logged out.", user.getFullName());

    }

    private String loginCase(String[] commands) {
        LoginDTO loginData = new LoginDTO(commands);
        Optional<User> user = this.userService.login(loginData);

        if (user.isPresent()) {
            return String.format("Successfully logged in %s.", user.get().getFullName());
        }

        return "Incorrect username or password.";

    }

    private String registerCase(String[] commands) {
        RegistrationDTO registrationData = new RegistrationDTO(commands);
        Optional<User> exists = this.userService.findByEmailAndPassword
                (registrationData.getEmail(), registrationData.getPassword());

        if (exists.isPresent()) {
            throw new UserAlreadyRegistered();
        }

        User user = this.userService.register(registrationData);

        return String.format("%s was registered.", user.getFullName());
    }

}
