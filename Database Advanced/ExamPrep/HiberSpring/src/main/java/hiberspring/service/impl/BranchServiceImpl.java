package hiberspring.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.ImportBranchDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final TownService townService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Validator validator;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, TownService townService, ValidationUtil validationUtil) {
        this.branchRepository = branchRepository;
        this.townService = townService;
        this.validationUtil = validationUtil;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return String.join("\n",
                Files.readAllLines(Path.of(Constants.PATH_TO_FILES + "branches.json")));
    }

    @Override
    public String importBranches(String branchesFileContent) {
        ImportBranchDTO[] branchDTOS = this.gson.fromJson(branchesFileContent, ImportBranchDTO[].class);

        return Arrays.stream(branchDTOS)
                .map(this::importBranch)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Branch> getBranchByName(String name) {
        return this.branchRepository.findByName(name);
    }

    private String importBranch(ImportBranchDTO importBranchDTO){

        Set<ConstraintViolation<ImportBranchDTO>> errors = this.validator.validate(importBranchDTO);

        if(!errors.isEmpty()){
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Optional<Town> optTown = this.townService.getTownByName(importBranchDTO.getTown());

        if(optTown.isEmpty()){
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Branch branch = this.modelMapper.map(importBranchDTO, Branch.class);
        branch.setTown(optTown.get());
        this.branchRepository.save(branch);

        return String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, "Branch", branch.getName());
    }

}
