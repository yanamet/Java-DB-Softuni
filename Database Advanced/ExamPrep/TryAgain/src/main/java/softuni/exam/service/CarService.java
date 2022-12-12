package softuni.exam.service;



import softuni.exam.models.entity.Car;

import java.io.IOException;
import java.util.Optional;

//ToDo - Before start App implement this Service and set areImported to return false
public interface CarService {

    boolean areImported();

    String readCarsFileContent() throws IOException;
	
	String importCars() throws IOException;

    String getCarsOrderByPicturesCountThenByMake();

    public Optional<Car> findCarById(long car);

}
