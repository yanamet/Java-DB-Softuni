package com.example.tryy.productShop.services;

import com.example.tryy.productShop.entities.categories.CategoryStats;
import com.example.tryy.productShop.entities.products.ProductWithoutBuyerDTO;
import com.example.tryy.productShop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductWithoutBuyerDTO> getProductsInPriceRangeForSell(float from, float to) {
        BigDecimal fromDec = BigDecimal.valueOf(from);
        BigDecimal toDec = BigDecimal.valueOf(to);
        return this.productRepository.findAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(fromDec, toDec);

    }

    @Override
    public List<CategoryStats> getCategoryStatistics() {
        return this.productRepository.getCategoryStats();

    }


}
