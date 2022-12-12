package exam.service.impl;

import exam.model.dto.ImportRootShopDTO;
import exam.model.dto.ImportShopDTO;
import exam.model.entity.Shop;
import exam.model.entity.Town;
import exam.repository.ShopRepository;
import exam.service.ShopService;
import exam.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
public class ShopServiceImpl implements ShopService {

    Path path = Path.of
            ("src", "main", "resources", "files", "xml", "shops.xml");

    private final ShopRepository shopRepository;
    private final TownService townService;
    private final Unmarshaller unmarshaller;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public ShopServiceImpl(ShopRepository shopRepository, TownService townService) throws JAXBException {
        this.shopRepository = shopRepository;
        this.townService = townService;

        JAXBContext context = JAXBContext.newInstance(ImportRootShopDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {

        ImportRootShopDTO unmarshal = (ImportRootShopDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return unmarshal.getShops().stream()
                .map(this::importShop)
                .collect(Collectors.joining("\n"));

    }

    @Override
    public Shop getShopByName(String name) {
        return this.shopRepository.findByName(name).get();
    }

    private String importShop(ImportShopDTO importShopDTO) {

        Set<ConstraintViolation<ImportShopDTO>> errors = this.validator.validate(importShopDTO);

        if (!errors.isEmpty()) {
            return "Invalid Shop";
        }

        Optional<Shop> optShop = this.shopRepository.findByName(importShopDTO.getName());

        if (optShop.isPresent()) {
            return "Invalid Shop";
        }

        Town town = this.townService.getTownByName(importShopDTO.getTown().getName());
        Shop shop = this.modelMapper.map(importShopDTO, Shop.class);
        shop.setTown(town);
        this.shopRepository.save(shop);

        return String.format("Successfully imported Shop %s - %.0f",
                shop.getName(), shop.getIncome());
    }

}
