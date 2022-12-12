package com.example.xmlexercise.productShop.services;


import com.example.xmlexercise.productShop.entities.categories.Category;
import com.example.xmlexercise.productShop.entities.categories.CategoryImportDTO;
import com.example.xmlexercise.productShop.entities.products.Product;
import com.example.xmlexercise.productShop.entities.products.ProductsImportDTO;
import com.example.xmlexercise.productShop.entities.users.User;
import com.example.xmlexercise.productShop.entities.users.UsersImportDTO;
import com.example.xmlexercise.productShop.repositories.CategoryRepository;
import com.example.xmlexercise.productShop.repositories.ProductRepository;
import com.example.xmlexercise.productShop.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeedServiceImpl implements SeedService {

    //src\_08_JSON\Tryy\categories.json
    private static final String USERS_XML_PATH = "src\\main\\resources\\productsShop\\users.xml";
    private static final String CATEGORIES_XML_PATH = "src\\main\\resources\\productsShop\\categories.xml";
    private static final String PRODUCTS_XML_PATH = "src\\main\\resources\\productsShop\\products.xml";
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;


    @Autowired
    public SeedServiceImpl(UserRepository userRepository, ProductRepository productRepository,
                           CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;

        this.mapper = new ModelMapper();
    }

    @Override
    public void seedUsers() throws FileNotFoundException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(UsersImportDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        FileReader fileReader = new FileReader(USERS_XML_PATH);
        UsersImportDTO usersImports = (UsersImportDTO) unmarshaller.unmarshal(fileReader);

        List<User> users = usersImports
                .getUsers()
                .stream()
                .map(user -> this.mapper.map(user, User.class))
                .collect(Collectors.toList());

        this.userRepository.saveAll(users);
    }



    @Override
    public void seedCategories() throws FileNotFoundException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(CategoryImportDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        FileReader fileReader = new FileReader(CATEGORIES_XML_PATH);
        CategoryImportDTO importDTO =(CategoryImportDTO) unmarshaller.unmarshal(fileReader);

        List<Category> categories = importDTO
                .getCategories()
                .stream()
                .map(ctg -> new Category(ctg.getName()))
                .collect(Collectors.toList());

        this.categoryRepository.saveAll(categories);
    }



    @Override
    public void seedProducts() throws FileNotFoundException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(ProductsImportDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        FileReader fileReader = new FileReader(PRODUCTS_XML_PATH);
        ProductsImportDTO productsDTO =(ProductsImportDTO) unmarshaller.unmarshal(fileReader);

        List<Product> products = productsDTO
                .getProducts()
                .stream()
                .map(dto -> this.mapper.map(dto, Product.class))
                .map(this::setRandomSeller)
                .map(this::setRandomBuyer)
                .map(this::setRandomCategory)
                .collect(Collectors.toList());

        this.productRepository.saveAll(products);

    }

    private Product setRandomCategory(Product product) {
        long categoriesCount = this.categoryRepository.count();
        Random random = new Random();

        HashSet<Category> categories = new HashSet<>();
        for (int i = 0; i < categoriesCount; i++) {
            int randomCatId = random.nextInt((int) categoriesCount) + 1;
            categories.add(this.categoryRepository.findById(randomCatId).get());
        }
        product.setCategories(categories);
        return product;
    }

    private Product setRandomBuyer(Product product) {
        if (product.getPrice().compareTo(BigDecimal.valueOf(940)) > 0) {
            return product;
        }

        long userCount = this.userRepository.count();
        int randomUserId = new Random().nextInt((int) userCount) + 1;

        Optional<User> user = this.userRepository.findById(randomUserId);
        product.setBuyer(user.get());
        return product;
    }

    private Product setRandomSeller(Product product) {
        long userCount = this.userRepository.count();
        int randomUserId = new Random().nextInt((int) userCount) + 1;
        Optional<User> user = this.userRepository.findById(randomUserId);
        product.setSeller(user.get());
        return product;
    }

}
