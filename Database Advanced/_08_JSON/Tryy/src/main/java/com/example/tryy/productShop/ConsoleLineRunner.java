package com.example.tryy.productShop;

import com.example.tryy.productShop.entities.categories.CategoryStats;
import com.example.tryy.productShop.entities.products.ProductWithoutBuyerDTO;
import com.example.tryy.productShop.entities.users.UserWithSoldProductsDTO;
import com.example.tryy.productShop.services.ProductService;
import com.example.tryy.productShop.services.SeedService;
import com.example.tryy.productShop.services.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsoleLineRunner implements CommandLineRunner {

    private final SeedService seedService;
    private final UserService userService;
    private final ProductService productService;
    private final Gson gson;



    @Autowired
    public ConsoleLineRunner(SeedService seedService, UserService userService, ProductService productService) {
        this.seedService = seedService;
        this.userService = userService;
        this.productService = productService;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void run(String... args) throws Exception {

//        seedService.seedProducts();

       // firstQueryProductsBetweenPrice();

        //secondQueryUsersWithSoldItems();

        //thirdQueryCategoryStats();


    }

    private void thirdQueryCategoryStats() {
        List<CategoryStats> categoryStatistics = this.productService.getCategoryStatistics();
        String json = this.gson.toJson(categoryStatistics);
        System.out.println(json);
    }

    private void secondQueryUsersWithSoldItems() {
        List<UserWithSoldProductsDTO> usersWithSoldProducts = this.userService.getUsersWithSoldProducts();

        String toJson = this.gson.toJson(usersWithSoldProducts);

        System.out.println(toJson);
    }

    private void firstQueryProductsBetweenPrice() {
        List<ProductWithoutBuyerDTO> productsForSell = this.productService
                .getProductsInPriceRangeForSell(500, 1000);

        String toJson = this.gson.toJson(productsForSell);
        System.out.println(toJson);
    }

}
