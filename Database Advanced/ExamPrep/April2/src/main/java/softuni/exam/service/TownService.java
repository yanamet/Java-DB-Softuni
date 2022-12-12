package softuni.exam.service;

import softuni.exam.models.entity.Town;

import java.io.IOException;
import java.util.Optional;

// TODO: Implement all methods


public interface TownService {

    boolean areImported();

    String readTownsFileContent() throws IOException;
	
	String importTowns() throws IOException;

    Town getTownByName(String town);

    Optional<Town> getOptionalTownByName(String town);
}
