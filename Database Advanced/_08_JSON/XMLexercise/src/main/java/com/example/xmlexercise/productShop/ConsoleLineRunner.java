package com.example.xmlexercise.productShop;

import com.example.xmlexercise.productShop.entities.categories.CategoriesExportDTO;
import com.example.xmlexercise.productShop.entities.products.ExportProductsInRange;
import com.example.xmlexercise.productShop.entities.users.ExportSellersDTO;
import com.example.xmlexercise.productShop.entities.users.ExportUsersWithSoldItemsDTO;
import com.example.xmlexercise.productShop.services.ProductService;
import com.example.xmlexercise.productShop.services.SeedService;
import com.example.xmlexercise.productShop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.List;

@Component
public class ConsoleLineRunner implements CommandLineRunner {

    private final SeedService seedService;
    private final ProductService productService;
    private final UserService userService;




    @Autowired
    public ConsoleLineRunner(SeedService seedService, ProductService productService, UserService userService) {
        this.seedService = seedService;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

//        this.seedService.seedUsers();
//        this.seedService.seedCategories();
//        this.seedService.seedProducts();

//        01. this.productsInRange();
//        02. this.findUsersWithSoldProducts();
        this.getCategoryStatistics();
        
    }

    private void getCategoryStatistics() throws JAXBException {
        CategoriesExportDTO categoryStatistics = this.productService.getCategoryStatistics();

        JAXBContext context = JAXBContext.newInstance(CategoriesExportDTO.class);
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(categoryStatistics, System.out);

    }

    private void findUsersWithSoldProducts() throws JAXBException {
        ExportSellersDTO users = this.userService.findAllWithSoldProducts();

        JAXBContext context = JAXBContext.newInstance(ExportSellersDTO.class);
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(users, System.out);
    }

    private void productsInRange() throws JAXBException {
        ExportProductsInRange inRange = this.productService.getInRange(500, 1000);

        JAXBContext context = JAXBContext.newInstance(ExportProductsInRange.class);
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(inRange, System.out);

    }


}
