package softuni.exam.service.impl;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportRootTaskDTO;
import softuni.exam.models.dto.ImportTaskDTO;
import softuni.exam.models.entity.*;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.MechanicService;
import softuni.exam.service.PartService;
import softuni.exam.service.TaskService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "tasks.xml");
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;

    private final TaskRepository taskRepository;
    private final ValidationUtil validationUtil;
    private final PartService partService;
    private final CarService carService;
    private final MechanicService mechanicService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           ValidationUtil validationUtil, PartService partService,
                           CarService carService, MechanicService mechanicService) throws JAXBException {
        this.taskRepository = taskRepository;
        this.validationUtil = validationUtil;
        this.partService = partService;
        this.carService = carService;
        this.mechanicService = mechanicService;

        this.modelMapper = new ModelMapper();
        this.modelMapper.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> context){
                return LocalDateTime.parse(context.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });

        JAXBContext context = JAXBContext.newInstance(ImportRootTaskDTO.class);
        this.unmarshaller = context.createUnmarshaller();
    }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {

        ImportRootTaskDTO taskRoot = (ImportRootTaskDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return taskRoot
                .getTasks()
                .stream()
                .map(this::importTask)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {
        List<Task> cars = this.taskRepository.findByCarCarTypeOrderByPriceDesc(CarType.coupe);
        return cars
                .stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));
    }

    private String importTask(ImportTaskDTO importTaskDTO){

        if(!this.validationUtil.isValid(importTaskDTO)){
            return "Invalid task";
        }

        Optional<Mechanic> optMechanic = this.mechanicService
                .getMechanicByFirstName(importTaskDTO.getMechanic().getFirstName());

        if(optMechanic.isEmpty()){
            return "Invalid task";
        }

      Optional<Car> optCar = this.carService.getCarById(importTaskDTO.getCar().getId());

        if(optCar.isEmpty()){
            return "Invalid task";
        }

        Part part = this.partService.getPartById(importTaskDTO.getPart().getId());

        Task task = this.modelMapper.map(importTaskDTO, Task.class);
        task.setCar(optCar.get());
        task.setMechanic(optMechanic.get());
        task.setPart(part);
        this.taskRepository.save(task);

        return String.format("Successfully imported task %.2f",
                task.getPrice());
    }

}
