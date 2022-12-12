package exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exam.model.dto.ImportLaptopDTO;
import exam.model.entity.Laptop;
import exam.model.entity.Shop;
import exam.model.entity.WarrantyType;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LaptopServiceImpl implements LaptopService {

    private final List<String> VALID_WARRANTY_TYPES = Arrays.asList("BASIC", "PREMIUM", "LIFETIME");

    Path path = Path.of
            ("src", "main", "resources", "files", "json", "laptops.json");

    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    private final LaptopRepository laptopRepository;
    private final ShopService shopService;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopService shopService) {
        this.laptopRepository = laptopRepository;
        this.shopService = shopService;

        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }


    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importLaptops() throws IOException {
        String json = this.readLaptopsFileContent();
        ImportLaptopDTO[] laptopDTOS = this.gson.fromJson(json, ImportLaptopDTO[].class);
        return Arrays.stream(laptopDTOS)
                .map(this::importLaptop)
                .collect(Collectors.joining("\n"));
    }

    private String importLaptop(ImportLaptopDTO importLaptopDTO) {
        Set<ConstraintViolation<ImportLaptopDTO>> errors = this.validator.validate(importLaptopDTO);

        if (!errors.isEmpty()) {
            return "Invalid Laptop";
        }

        Optional<Laptop> optLaptop = this.laptopRepository.findByMacAddress(importLaptopDTO.getMacAddress());

        if (optLaptop.isPresent()) {
            return "Invalid Laptop";
        }

        if(!VALID_WARRANTY_TYPES.contains(importLaptopDTO.getWarrantyType())){
            return "Invalid Laptop";
        }

        Shop shop = this.shopService.getShopByName(importLaptopDTO.getShop().getName());
        WarrantyType warrantyType = WarrantyType.valueOf(importLaptopDTO.getWarrantyType());
        Laptop laptop = this.modelMapper.map(importLaptopDTO, Laptop.class);
        laptop.setShop(shop);
        laptop.setWarrantyType(warrantyType);
        this.laptopRepository.save(laptop);

        return String.format("Successfully imported Laptop %s - %.2f - %d - %d",
                laptop.getMacAddress(), laptop.getCpuSpeed(), laptop.getRam(), laptop.getStorage());


    }

    @Override
    public String exportBestLaptops() {
        List<Laptop> laptops = this.laptopRepository
                .findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc();
        return laptops.stream().map(Laptop::toString).collect(Collectors.joining("\n"));
    }
}
