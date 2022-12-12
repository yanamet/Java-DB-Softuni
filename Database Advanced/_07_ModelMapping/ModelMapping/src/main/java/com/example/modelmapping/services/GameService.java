package com.example.modelmapping.services;

import com.example.modelmapping.entities.games.Game;
import com.example.modelmapping.entities.games.GameDTO;

import java.util.List;
import java.util.Optional;

public interface GameService {
    void addGame(Game game);

    Optional<Game> findByTitle(String title);

    Game findGameById(int id);

    void save(Game game);

    void deleteGame(Game game);

    List<Game> getAllGames();

    Optional<Game> findGameByTitle(String gameTitle);
}
