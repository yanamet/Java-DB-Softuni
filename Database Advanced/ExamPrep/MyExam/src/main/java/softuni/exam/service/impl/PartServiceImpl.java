package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPartDTO;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartServiceImpl implements PartService {

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "parts.json");

    private final Gson gson;
    private final ModelMapper modelMapper;

    private final ValidationUtil validationUtil;

    private final PartRepository partRepository;


    @Autowired
    public PartServiceImpl(ValidationUtil validationUtil, PartRepository partRepository) {
        this.partRepository = partRepository;
        this.validationUtil = validationUtil;

        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
    }

    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importParts() throws IOException {
        String json = this.readPartsFileContent();
        ImportPartDTO[] partDTOS = this.gson.fromJson(json, ImportPartDTO[].class);
        return Arrays.stream(partDTOS)
                .map(this::importPart)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Part getPartById(long id) {
        return this.partRepository.findById(id).get();
    }

    private String importPart(ImportPartDTO importPartDTO){

        if(!this.validationUtil.isValid(importPartDTO)){
            return "Invalid part";
        }

       Optional<Part> optPart = this.partRepository
               .findByPartName(importPartDTO.getPartName());

        if(optPart.isPresent()){
            return "Invalid part";
        }

        Part part = this.modelMapper.map(importPartDTO, Part.class);
        this.partRepository.save(part);

        return String.format("Successfully imported part %s - %.2f",
                part.getPartName(), part.getPrice());
    }

}
