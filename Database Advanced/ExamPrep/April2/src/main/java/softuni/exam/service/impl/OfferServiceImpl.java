package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportOfferDTO;
import softuni.exam.models.dto.ImportOfferRootDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;

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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "offers.xml");

    private final OfferRepository offerRepository;
    private final Unmarshaller unmarshaller;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final AgentService agentService;
    private final ApartmentService apartmentService;

    public OfferServiceImpl(OfferRepository offerRepository, AgentService agentService, ApartmentService apartmentService) throws JAXBException {
        this.offerRepository = offerRepository;
        this.agentService = agentService;
        this.apartmentService = apartmentService;


        JAXBContext context = JAXBContext.newInstance(ImportOfferRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();

        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        ImportOfferRootDTO offerRootDTO = (ImportOfferRootDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return offerRootDTO.getOffers()
                .stream()
                .map(this::importOffer)
                .collect(Collectors.joining("\n"));

    }

    private String importOffer(ImportOfferDTO importOfferDTO) {
        Set<ConstraintViolation<ImportOfferDTO>> errors = this.validator.validate(importOfferDTO);

        if (!errors.isEmpty()) {
            return "Invalid offer";
        }


        Optional<Agent> optAgent = this.agentService
                .getAgentByName(importOfferDTO.getName().getName());

        if(optAgent.isEmpty()){
            return "Invalid offer";
        }

        Offer offer = this.modelMapper.map(importOfferDTO, Offer.class);

        offer.setAgent(agentService.getAgentByName(importOfferDTO.getName().getName()).get());
        offer.setApartment(apartmentService.getApartmentById(importOfferDTO.getApartment().getId()));

        this.offerRepository.save(offer);
        return String.format("Successfully imported offer %.2f", offer.getPrice());
    }

    @Override
    public String exportOffers() {
        List<Offer> offers = this.offerRepository
                .findByApartmentApartmentTypeOrderByApartmentAreaDescPriceAsc(ApartmentType.three_rooms);
        return offers
                .stream()
                .map(Offer::toString)
                .collect(Collectors.joining("\n"));
    }
}
