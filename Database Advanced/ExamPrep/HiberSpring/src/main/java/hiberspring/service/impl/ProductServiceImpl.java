package hiberspring.service.impl;


import hiberspring.common.Constants;
import hiberspring.domain.dtos.ImportProductDTO;
import hiberspring.domain.dtos.ImportRootProductDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.ProductRepository;
import hiberspring.service.BranchService;
import hiberspring.service.ProductService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BranchService branchService;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshal;

    public ProductServiceImpl(ProductRepository productRepository, BranchService branchService, ValidationUtil validationUtil) throws JAXBException {
        this.productRepository = productRepository;
        this.branchService = branchService;
        this.validationUtil = validationUtil;

        JAXBContext context = JAXBContext.newInstance(ImportRootProductDTO.class);
        this.unmarshal = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();

    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return String.join("\n",
                Files.readAllLines(Path.of(Constants.PATH_TO_FILES + "products.xml")));
    }

    @Override
    public String importProducts() throws JAXBException, FileNotFoundException {

        ImportRootProductDTO products = (ImportRootProductDTO) this.unmarshal
                .unmarshal(new FileReader(Constants.PATH_TO_FILES + "products.xml"));

        return products
                .getProducts()
                .stream()
                .map(this::importProduct)
                .collect(Collectors.joining("\n"));
    }

    private String importProduct(ImportProductDTO importProductDTO) {

        if (!this.validationUtil.isValid(importProductDTO)) {
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Optional<Branch> optBranch = this.branchService.getBranchByName(importProductDTO.getBranch());

        if (optBranch.isEmpty()) {
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Optional<Product> optProduct = this.productRepository.findByName(importProductDTO.getName());

        if(optProduct.isPresent()){
            return Constants.INCORRECT_DATA_MESSAGE;
        }

        Product product = this.modelMapper.map(importProductDTO, Product.class);
        product.setBranch(optBranch.get());
        this.productRepository.save(product);

        return String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, "Product", product.getName());
    }

}
