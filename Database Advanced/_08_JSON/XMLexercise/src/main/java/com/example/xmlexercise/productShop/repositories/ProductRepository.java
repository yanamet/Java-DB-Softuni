package com.example.xmlexercise.productShop.repositories;

import com.example.xmlexercise.productShop.entities.categories.CategoryStats;
import com.example.xmlexercise.productShop.entities.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(BigDecimal fromDec, BigDecimal toDec);

    @Query("SELECT new com.example.xmlexercise.productShop.entities.categories.CategoryStats(" +
            "c.name, COUNT(p), avg(p.price), sum(p.price)) " +
            " FROM Product p" +
            " JOIN p.categories c" +
            " GROUP BY c ")
    List<CategoryStats> getCategoryStats();

}

