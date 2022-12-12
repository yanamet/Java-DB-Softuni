package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.dto.ImportPictureDTO;
import softuni.exam.domain.entities.dto.ImportRootPictureDTO;
import softuni.exam.repository.PictureRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "pictures.xml");
    private final Unmarshaller unmarshaller;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final PictureRepository pictureRepository;

    public PictureServiceImpl(PictureRepository pictureRepository) throws JAXBException {
        this.pictureRepository = pictureRepository;


        JAXBContext context = JAXBContext.newInstance(ImportRootPictureDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Override
    public String importPictures() throws FileNotFoundException, JAXBException {

        ImportRootPictureDTO unmarshal = (ImportRootPictureDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return unmarshal.getPictures()
                .stream()
                .map(this::importPicture)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public Optional<Picture> getPicByUrl(String url) {
        return this.pictureRepository.findByUrl(url);
    }

    private String importPicture(ImportPictureDTO importPictureDTO){

        Set<ConstraintViolation<ImportPictureDTO>> errors = this.validator.validate(importPictureDTO);

        if(!errors.isEmpty()){
            return "Invalid picture";
        }

        if(importPictureDTO.getUrl() == null){
            return "Invalid picture";

        }

        Picture picture = this.modelMapper.map(importPictureDTO, Picture.class);
        this.pictureRepository.save(picture);

        return String.format("Successfully imported picture - %s", picture.getUrl());
    }

}
