package softuni.exam.service.impl;

import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportApartmentDTO;
import softuni.exam.models.dto.ImportApartmentRootDTO;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "apartments.xml");

    private final ApartmentRepository apartmentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final Unmarshaller unmarshaller;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownService townService)
            throws JAXBException {
        this.apartmentRepository = apartmentRepository;
        this.townService = townService;

        JAXBContext context = JAXBContext.newInstance(ImportApartmentRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        ImportApartmentRootDTO rootApartment = (ImportApartmentRootDTO)
                this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return rootApartment.getApartments()
                .stream()
                .map(this::importApartment)
                .collect(Collectors.joining("\n"));

    }

    @Override
    public Apartment getApartmentById(long id) {
        return this.apartmentRepository.findById(id).get();
    }

    private String importApartment(ImportApartmentDTO importApartmentDTO){
        Set<ConstraintViolation<ImportApartmentDTO>> errors = this.validator
                .validate(importApartmentDTO);

        if(!errors.isEmpty()){
            return "Invalid apartment";
        }

        Town town = this.townService.getOptionalTownByName(importApartmentDTO.getTown()).get();

        Optional<Apartment> optApart = this.apartmentRepository.findByTownAndArea
                (town, importApartmentDTO.getArea());

        if(optApart.isPresent()){
            return "Invalid apartment";
        }

        Apartment apartment = this.modelMapper.map(importApartmentDTO, Apartment.class);
        apartment.setTown(town);

        this.apartmentRepository.save(apartment);

        return String.format("Successfully imported apartment %s - %.2f",
                apartment.getApartmentType().toString(), apartment.getArea());

    }

}
