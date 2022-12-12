package softuni.exam.service;


import softuni.exam.models.entity.Plane;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

//ToDo - Before start App implement this Service and set areImported to return false
public interface PlaneService {

    boolean areImported();

    String readPlanesFileContent() throws IOException;
	
	String importPlanes() throws FileNotFoundException, JAXBException;

    Optional<Plane> getByRegisterNumber(String registerNumber);
}
