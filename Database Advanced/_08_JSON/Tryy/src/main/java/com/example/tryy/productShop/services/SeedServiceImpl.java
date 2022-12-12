package com.example.tryy.productShop.services;

import com.example.tryy.productShop.entities.categories.CategoriesImportDTO;
import com.example.tryy.productShop.entities.categories.Category;
import com.example.tryy.productShop.entities.products.Product;
import com.example.tryy.productShop.entities.products.ProductsImportDTO;
import com.example.tryy.productShop.entities.users.User;
import com.example.tryy.productShop.entities.users.UserImportDTO;
import com.example.tryy.productShop.repositories.CategoryRepository;
import com.example.tryy.productShop.repositories.ProductRepository;
import com.example.tryy.productShop.repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeedServiceImpl implements SeedService {

    //src\_08_JSON\Tryy\categories.json
    private static final String USERS_JSON_PATH = "src\\main\\resources\\productsShop\\users.json";
    private static final String CATEGORIES_JSON_PATH = "src\\main\\resources\\productsShop\\categories.json";
    private static final String PRODUCTS_JSON_PATH = "src\\main\\resources\\productsShop\\products.json";
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public SeedServiceImpl(UserRepository userRepository, ProductRepository productRepository,
                           CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;

        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void seedUsers() throws FileNotFoundException {
        FileReader fileReader = new FileReader(USERS_JSON_PATH);
        UserImportDTO[] usersDTO = this.gson.fromJson(fileReader, UserImportDTO[].class);

        List<User> users = Arrays.stream(usersDTO)
                .map(importDTO -> this.modelMapper.map(importDTO, User.class))
                .collect(Collectors.toList());

        this.userRepository.saveAll(users);
    }

    @Override
    public void seedCategories() throws FileNotFoundException {
        FileReader fileReader = new FileReader(CATEGORIES_JSON_PATH);
        CategoriesImportDTO[] categoriesDTO = this.gson.fromJson(fileReader, CategoriesImportDTO[].class);

        List<Category> categories = Arrays.stream(categoriesDTO)
                .map(importDto -> this.modelMapper.map(importDto, Category.class))
                .collect(Collectors.toList());

        this.categoryRepository.saveAll(categories);
    }

    @Override
    public void seedProducts() throws FileNotFoundException {
        FileReader fileReader = new FileReader(PRODUCTS_JSON_PATH);
        ProductsImportDTO[] productsDTO = this.gson.fromJson(fileReader, ProductsImportDTO[].class);

        List<Product> products = Arrays.stream(productsDTO)
                .map(importDto -> this.modelMapper.map(importDto, Product.class))
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
