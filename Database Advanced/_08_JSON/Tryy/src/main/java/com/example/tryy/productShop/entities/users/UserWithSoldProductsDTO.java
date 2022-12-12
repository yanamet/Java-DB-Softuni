package com.example.tryy.productShop.entities.users;

import com.example.tryy.productShop.entities.products.SoldProductsDTO;

import java.util.List;

public class UserWithSoldProductsDTO {
    private String firstName;
    private String lastName;
    private List<SoldProductsDTO> sellingItems;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSellingItems(List<SoldProductsDTO> sellingItems) {
        this.sellingItems = sellingItems;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<SoldProductsDTO> getSellingItems() {
        return sellingItems;
    }
}
