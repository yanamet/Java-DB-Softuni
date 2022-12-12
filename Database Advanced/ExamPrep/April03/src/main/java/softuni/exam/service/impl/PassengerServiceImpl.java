package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPassengerDTO;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "passengers.json");
    private final PassengerRepository passengerRepository;
    private final TownService townService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, TownService townService, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.passengerRepository = passengerRepository;
        this.townService = townService;
        this.validationUtil = validationUtil;

        this.gson = new GsonBuilder().create();
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importPassengers() throws IOException {

        String json = this.readPassengersFileContent();
        ImportPassengerDTO[] passengerDTOS = this.gson.fromJson(json, ImportPassengerDTO[].class);

        return Arrays.stream(passengerDTOS)
                .map(this::importPassenger)
                .collect(Collectors.joining("\n"));
    }

    private String importPassenger(ImportPassengerDTO importPassengerDTO){

        if(!this.validationUtil.isValid(importPassengerDTO)){
            return "Invalid Passenger";
        }

        Optional<Passenger> optionalPassenger = this.passengerRepository
                .findByEmail(importPassengerDTO.getEmail());

        if(optionalPassenger.isPresent()){
            return "Invalid Passenger";
        }

       Optional<Town> optTown = this.townService.getTownByName(importPassengerDTO.getTown());

        if(optTown.isEmpty()){
            return "Invalid Passenger";
        }

        Passenger passenger = this.modelMapper.map(importPassengerDTO, Passenger.class);
        passenger.setTown(optTown.get());
        this.passengerRepository.save(passenger);

        return String.format("Successfully imported Passenger %s - %s",
                passenger.getLastName(), passenger.getEmail());
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        return this.passengerRepository
                .findAllPassengers()
                .stream()
                .map(Passenger::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Passenger> getPassegerByEmail(String email) {
        return this.passengerRepository.findByEmail(email);
    }
}
