package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPictureDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "pictures.json");

    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    private final PictureRepository pictureRepository;
    private final CarService carService;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarService carService) {
        this.pictureRepository = pictureRepository;
        this.carService = carService;

        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();


        this.modelMapper.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> context){
                return LocalDateTime.parse(context.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importPictures() throws IOException {
        String json = this.readPicturesFromFile();
        ImportPictureDTO[] importPictureDTOS = this.gson.fromJson(json, ImportPictureDTO[].class);
        return Arrays.stream(importPictureDTOS)
                .map(this::importPicture)
                .collect(Collectors.joining("\n"));

    }

    private String importPicture(ImportPictureDTO importPictureDTO) {
        Set<ConstraintViolation<ImportPictureDTO>> errors = this.validator.validate(importPictureDTO);

        if(!errors.isEmpty()){
            return "Invalid picture";
        }

        Optional<Picture> optPic = this.pictureRepository.findByName(importPictureDTO.getName());

        if(optPic.isPresent()){
            return "Invalid picture";
        }

        Optional<Car> optCar = this.carService.findCarById(importPictureDTO.getCar());

        if(optCar.isEmpty()){
            return "Invalid picture";
        }

        Car car = optCar.get();
        Picture picture = this.modelMapper.map(importPictureDTO, Picture.class);
        picture.setCar(car);

        this.pictureRepository.save(picture);

        return String.format("Successfully import picture - %s", picture.getName());

    }

}
