package com.example.xmlexercise.productShop.services;


import com.example.xmlexercise.productShop.entities.categories.CategoriesExportDTO;
import com.example.xmlexercise.productShop.entities.products.ExportProductsInRange;

public interface ProductService {

    ExportProductsInRange getInRange(float from, float to);

    CategoriesExportDTO getCategoryStatistics();
}

