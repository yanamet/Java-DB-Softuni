package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportAgentDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;

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
public class AgentServiceImpl implements AgentService {
    Path path = Path.of
            ("src", "main", "resources", "files", "json", "agents.json");
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    private final AgentRepository agentRepository;
    private final TownService townService;

    public AgentServiceImpl(AgentRepository agentRepository, TownService townService) {
        this.agentRepository = agentRepository;
        this.townService = townService;

        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importAgents() throws IOException {
        String json = this.readAgentsFromFile();
        ImportAgentDTO[] agentsDTO = this.gson.fromJson(json, ImportAgentDTO[].class);

        return Arrays.stream(agentsDTO)
                .map(this::importAgent)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<Agent> getAgentByName(String name) {
        return this.agentRepository.findByFirstName(name);
    }

    private String importAgent(ImportAgentDTO agentDTO){
        Set<ConstraintViolation<ImportAgentDTO>> errors = this.validator.validate(agentDTO);

        if(!errors.isEmpty()){
            return "Invalid agent";
        }

        Optional<Agent> optAgent = this.agentRepository.findByFirstName(agentDTO.getFirstName());


        if(optAgent.isPresent()){
            return "Invalid agent";
        }

        Town town = this.townService.getTownByName(agentDTO.getTown());
        Agent agent = this.modelMapper.map(agentDTO, Agent.class);
        agent.setTown(town);

        this.agentRepository.save(agent);

        return String.format("Successfully imported agent - %s %s",
                agent.getFirstName(), agent.getLastName());

    }

}
