package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportRootSellerDTO;
import softuni.exam.models.dto.ImportSellerDTO;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.XmlParser;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SellerServiceImpl implements SellerService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "sellers.xml");

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final XmlParser xmlParser;


    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, XmlParser xmlParser){
        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        ImportRootSellerDTO importRootSellerDTO = this.xmlParser
                .fromFile(path.toAbsolutePath().toString(), ImportRootSellerDTO.class);

        return null;

//        return importRootSellerDTO.getSellers()
//                .stream()
//                .map(this::importSeller)
//                .collect(Collectors.joining("\n"));

    }

    private String importSeller(ImportSellerDTO importSellerDTO){
        Set<ConstraintViolation<ImportSellerDTO>> errors = this.validator.validate(importSellerDTO);

        if(!errors.isEmpty()){
            return "Invalid seller";
        }

        Optional<Seller> optSeller = this.sellerRepository.findByEmail(importSellerDTO.getEmail());

        if(optSeller.isPresent()){
            return "Invalid seller";
        }

        Seller seller = this.modelMapper.map(importSellerDTO, Seller.class);

        this.sellerRepository.save(seller);

        return String.format("Successfully import seller %s - %s",
                seller.getFirstName(), seller.getEmail());

    }

}
