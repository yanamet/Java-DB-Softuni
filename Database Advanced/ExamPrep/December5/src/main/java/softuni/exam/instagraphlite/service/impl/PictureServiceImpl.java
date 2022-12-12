package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.ImportPictureDTO;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {

    Path path = Path.of
            ("src", "main", "resources", "files", "pictures.json");
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;


    private final PictureRepository pictureRepository;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;

        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importPictures() throws IOException {
        String json = this.readFromFileContent();
        ImportPictureDTO[] pictureDTOS = this.gson.fromJson(json, ImportPictureDTO[].class);
        return Arrays.stream(pictureDTOS)
                .map(this::importPicture)
                .collect(Collectors.joining("\n"));
    }

    private String importPicture(ImportPictureDTO importPictureDTO){
        Set<ConstraintViolation<ImportPictureDTO>> errors = this.validator.validate(importPictureDTO);

        if(!errors.isEmpty()){
            return "Invalid Picture";
        }

        Optional<Picture> optPic = this.pictureRepository.findByPath(importPictureDTO.getPath());

        if(optPic.isPresent()){
            return "Invalid Picture";
        }

        Picture picture = this.modelMapper.map(importPictureDTO, Picture.class);
        this.pictureRepository.save(picture);

                return String.format("Successfully imported Picture, with size %.2f",
                        picture.getSize());

    }

    @Override
    public String exportPictures() {
        List<Picture> pictures = this.pictureRepository.findBySizeGreaterThanOrderBySizeAsc(30000);
         return pictures.stream().map(Picture::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Picture> getPictureByPath(String profilePicture) {
        return this.pictureRepository.findByPath(profilePicture);
    }


}
