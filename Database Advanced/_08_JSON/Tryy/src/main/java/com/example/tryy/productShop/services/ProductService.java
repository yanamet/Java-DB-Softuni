package com.example.tryy.productShop.services;

import com.example.tryy.productShop.entities.categories.CategoryStats;
import com.example.tryy.productShop.entities.products.ProductWithoutBuyerDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductWithoutBuyerDTO> getProductsInPriceRangeForSell(float from, float to);


    List<CategoryStats> getCategoryStatistics();
}

