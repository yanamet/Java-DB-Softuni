package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCarDTO;
import softuni.exam.models.dto.ImportRootCarDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "cars.xml");

    private final Unmarshaller unmarshaller;
    private final ModelMapper modelMapper;
    private final CarRepository carRepository;
    private final ValidationUtil validationUtil;

    public CarServiceImpl(CarRepository carRepository, ValidationUtil validationUtil) throws JAXBException {
        this.carRepository = carRepository;
        this.validationUtil = validationUtil;

        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportRootCarDTO.class);
        this.unmarshaller = context.createUnmarshaller();


    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importCars() throws IOException, JAXBException {

        ImportRootCarDTO carRoot = (ImportRootCarDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return carRoot
                .getCars()
                .stream()
                .map(this::importCar)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Car> getCarById(long id) {
        return this.carRepository.findById(id);
    }

    private String importCar(ImportCarDTO importCarDTO) {

        if (!this.validationUtil.isValid(importCarDTO)){
            return "Invalid car";
        }

        Optional<Car> optCar = this.carRepository
                .findByPlateNumber(importCarDTO.getPlateNumber());

        if(optCar.isPresent()){
            return "Invalid car";
        }

        Car car = this.modelMapper.map(importCarDTO, Car.class);
        this.carRepository.save(car);

        return String.format("Successfully imported car %s - %s",
                car.getCarMake(), car.getCarModel());
    }

}
