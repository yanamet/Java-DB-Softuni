package softuni.exam.service.impl;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportRootPlaneDTO;
import softuni.exam.models.dto.ImportRootTicketDTO;
import softuni.exam.models.dto.ImportTicketDTO;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Plane;
import softuni.exam.models.entity.Ticket;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "tickets.xml");

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Unmarshaller unmarshaller;
    private final TownService townService;
    private final PassengerService passengerService;
    private final PlaneService planeService;

    public TicketServiceImpl(TicketRepository ticketRepository, ModelMapper modelMapper,
                             ValidationUtil validationUtil,
                             TownService townService, PassengerService passengerService,
                             PlaneService planeService) throws JAXBException {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;

        this.modelMapper.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> context){
                return LocalDateTime.parse(context.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });

        this.validationUtil = validationUtil;
        JAXBContext context = JAXBContext.newInstance(ImportRootTicketDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.townService = townService;
        this.passengerService = passengerService;
        this.planeService = planeService;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importTickets() throws FileNotFoundException, JAXBException {

        ImportRootTicketDTO ticketsRoot = (ImportRootTicketDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return ticketsRoot
                .getTickets()
                .stream()
                .map(this::importTicket)
                .collect(Collectors.joining("\n"));
    }

    private String importTicket(ImportTicketDTO importTicketDTO){

        if(!this.validationUtil.isValid(importTicketDTO)){
            return "Invalid Ticket";
        }

        Optional<Ticket> optTicket = this.ticketRepository
                .findBySerialNumber(importTicketDTO.getSerialNumber());

        if(optTicket.isPresent()){
            return "Invalid Ticket";
        }

        Optional<Town> optFromTown = this.townService
                .getTownByName(importTicketDTO.getFromTown().getName());

        if(optFromTown.isEmpty()){
            return "Invalid Ticket";
        }

        Optional<Town> optToTown = this.townService
                .getTownByName(importTicketDTO.getToTown().getName());

        if(optToTown.isEmpty()){
            return "Invalid Ticket";
        }

       Optional<Passenger> optPassenger = this.passengerService
               .getPassegerByEmail(importTicketDTO.getPassenger().getEmail());

        if(optPassenger.isEmpty()){
            return "Invalid Ticket";
        }

       Optional<Plane> optPlane = this.planeService
               .getByRegisterNumber(importTicketDTO.getPlane().getRegisterNumber());

        if(optPlane.isEmpty()){
            return "Invalid Ticket";
        }

        Ticket ticket = this.modelMapper.map(importTicketDTO, Ticket.class);
        ticket.setFromTown(optFromTown.get());
        ticket.setToTown(optToTown.get());
        ticket.setPassenger(optPassenger.get());
        ticket.setPlane(optPlane.get());
        this.ticketRepository.save(ticket);

        return String.format("Successfully imported Ticket %s - %s",
                ticket.getFromTown().getName(), ticket.getToTown().getName());
    }

}
