package com.example.tryy.productShop.services;

import com.example.tryy.productShop.entities.users.User;
import com.example.tryy.productShop.entities.users.UserWithSoldProductsDTO;

import java.util.List;

public interface UserService {
    List<UserWithSoldProductsDTO> getUsersWithSoldProducts();

}
