package com.example.modelmapping.repositories;

import com.example.modelmapping.entities.games.Game;
import com.example.modelmapping.entities.games.GameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    Optional<Game> findByTitle(String title);

    Game findById(int id);



}
