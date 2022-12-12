package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCarDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CarServiceImpl implements CarService {
    Path path = Path.of
            ("src", "main", "resources", "files", "json", "cars.json");
    private final CarRepository carRepository;

    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;

        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();
        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importCars() throws IOException {
        String json = this.readCarsFileContent();
        ImportCarDTO[] importCarDTOS = this.gson.fromJson(json, ImportCarDTO[].class);
        return Arrays.stream(importCarDTOS)
                .map(this::importCar)
                .collect(Collectors.joining("\n"));

    }

    private String importCar(ImportCarDTO importCarDTO){
        Set<ConstraintViolation<ImportCarDTO>> errors = this.validator.validate(importCarDTO);

        if(!errors.isEmpty()){
            return "Invalid car";
        }

        Optional<Car> optCar = this.carRepository.findByMakeAndModelAndKilometers
                (importCarDTO.getMake(), importCarDTO.getModel(), importCarDTO.getKilometers());

        if(optCar.isPresent()){
            return "Invalid car";
        }

        Car car = this.modelMapper.map(importCarDTO, Car.class);
        this.carRepository.save(car);

        return String.format("Successfully imported car - %s - %s", car.getMake(),
                car.getModel());
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        return null;
    }

    @Override
    public Optional<Car> findCarById(long car) {
        return this.carRepository.findById(car);
    }
}
