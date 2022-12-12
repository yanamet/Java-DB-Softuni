package com.example.xmlexercise.productShop.services;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface SeedService {

    void seedUsers() throws FileNotFoundException, JAXBException;
    void seedProducts() throws FileNotFoundException, JAXBException;
    void seedCategories() throws FileNotFoundException, JAXBException;

    default void seedAll() throws FileNotFoundException, JAXBException {
        seedUsers();
        seedProducts();
        seedCategories();
    }

}
