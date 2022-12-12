package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCityDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;

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
public class CityServiceImpl implements CityService {

    private final Path path = Path.of
            ("src", "main", "resources", "files", "json", "cities.json");

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;

        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importCities() throws IOException {

        String json = this.readCitiesFileContent();
        ImportCityDTO[] importCityDTOS = this.gson.fromJson(json, ImportCityDTO[].class);
        return Arrays.stream(importCityDTOS)
                .map(this::importCity).collect(Collectors.joining("\n"));
    }

    private String importCity(ImportCityDTO importCityDTO){
        Set<ConstraintViolation<ImportCityDTO>> errors = this.validator.validate(importCityDTO);

        if(!errors.isEmpty()){
            return "Invalid city";
        }

        Optional<City> optCity = this.cityRepository.findByCityName(importCityDTO.getCityName());

        if(optCity.isPresent()){
            return "Invalid city";
        }

        City city = this.modelMapper.map(importCityDTO, City.class);
        Country country = this.countryRepository.findById(importCityDTO.getCountry()).get();

        city.setCountry(country);
        this.cityRepository.save(city);

        return String.format("Successfully imported city %s - %d",
                city.getCityName(), city.getPopulation());

    }

}
