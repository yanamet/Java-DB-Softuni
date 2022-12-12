package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPlaneDTO;
import softuni.exam.models.dto.ImportRootPlaneDTO;
import softuni.exam.models.entity.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaneServiceImpl implements PlaneService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "planes.xml");

    private final PlaneRepository planeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Unmarshaller unmarshaller;

    public PlaneServiceImpl(PlaneRepository planeRepository,
                            ModelMapper modelMapper, ValidationUtil validationUtil) throws JAXBException {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;

        JAXBContext context = JAXBContext.newInstance(ImportRootPlaneDTO.class);
        this.unmarshaller = context.createUnmarshaller();
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importPlanes() throws FileNotFoundException, JAXBException {

        ImportRootPlaneDTO planesRoot = (ImportRootPlaneDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return planesRoot
                .getPlanes()
                .stream()
                .map(this::importPlane)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Plane> getByRegisterNumber(String registerNumber) {
        return this.planeRepository.findByRegisterNumber(registerNumber);
    }

    private String importPlane(ImportPlaneDTO importPlaneDTO){

        if(!this.validationUtil.isValid(importPlaneDTO)){
            return "Invalid Plane";
        }

        Optional<Plane> optPlane = this.planeRepository
                .findByRegisterNumber(importPlaneDTO.getRegisterNumber());

        if(optPlane.isPresent()){
            return "Invalid Plane";
        }

        Plane plane = this.modelMapper.map(importPlaneDTO, Plane.class);
        this.planeRepository.save(plane);

        return String.format("Successfully imported Plane %s", plane.getRegisterNumber());
    }

}
