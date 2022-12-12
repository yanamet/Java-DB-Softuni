package com.example.modelmapping.services.impl;

import com.example.modelmapping.entities.games.Game;
import com.example.modelmapping.entities.games.GameDTO;
import com.example.modelmapping.repositories.GameRepository;
import com.example.modelmapping.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    @Override
    public void addGame(Game game) {
        this.gameRepository.save(game);
    }

    @Override
    public Optional<Game> findByTitle(String title) {
        return this.gameRepository.findByTitle(title);
    }

    @Override
    public Game findGameById(int id) {
        return this.gameRepository.findById(id);
    }

    @Override
    public void save(Game game) {
        this.gameRepository.save(game);
    }

    @Override
    public void deleteGame(Game game) {
        this.gameRepository.delete(game);
    }

    @Override
    public List<Game> getAllGames() {
        return this.gameRepository.findAll();
    }

    @Override
    public Optional<Game> findGameByTitle(String gameTitle) {
        return this.gameRepository.findByTitle(gameTitle);
    }
}
