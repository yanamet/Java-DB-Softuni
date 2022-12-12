package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportForecastDTO;
import softuni.exam.models.dto.ImportSingleForecastDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final Path path = Path.of
            ("src", "main", "resources", "files", "xml", "forecasts.xml");

    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final Unmarshaller unmarshaller;

    public ForecastServiceImpl(ForecastRepository forecastRepository, CityRepository cityRepository) throws JAXBException {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;

        JAXBContext context = JAXBContext.newInstance(ImportForecastDTO.class);
        this.unmarshaller = context.createUnmarshaller();

        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.modelMapper.addConverter(ctx -> LocalTime.parse(ctx.getSource(), DateTimeFormatter.ofPattern("H:mm:ss")),
                String.class, LocalTime.class);

    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        ImportForecastDTO forecastsDTO = (ImportForecastDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return forecastsDTO.getForecasts().stream()
                .map(this::importForecast).collect(Collectors.joining("\n"));

    }

    @Override
    public String exportForecasts() {
        List<Forecast> forecasts = this.forecastRepository
                .findByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc
                        (DaysOfWeek.SUNDAY, 150000);

        return forecasts.stream()
                .map(Forecast::toString).collect(Collectors.joining("\n"));
    }

    private String importForecast(ImportSingleForecastDTO forecastDTO){

        Set<ConstraintViolation<ImportSingleForecastDTO>> errors = this.validator
                .validate(forecastDTO);

        if(!errors.isEmpty()){
            return "Invalid forecast";
        }

        City city = this.cityRepository.findById(forecastDTO.getCity()).get();

        Optional<Forecast> optForecast = this.forecastRepository
                .findByCityAndDayOfWeek(city, forecastDTO.getDayOfWeek());

        if(optForecast.isPresent()){
            return "Invalid forecast";
        }

        Forecast forecast = this.modelMapper.map(forecastDTO, Forecast.class);
        forecast.setCity(city);
        this.forecastRepository.save(forecast);

        return String.format("Successfully import forecast %s - %.2f",
                forecast.getDayOfWeek().toString(), forecast.getMaxTemperature());

    }

}
