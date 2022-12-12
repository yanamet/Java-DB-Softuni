package com.example.xmlexercise.productShop.services;


import com.example.xmlexercise.productShop.entities.users.ExportSellersDTO;

import java.util.List;

public interface UserService {

    ExportSellersDTO findAllWithSoldProducts();
}
