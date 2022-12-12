package com.example.tryy.productShop.repositories;

import com.example.tryy.productShop.entities.categories.CategoryStats;
import com.example.tryy.productShop.entities.products.Product;
import com.example.tryy.productShop.entities.products.ProductWithoutBuyerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT new com.example.tryy.productShop.entities.products.ProductWithoutBuyerDTO" +
            "(p.name, p.price, p.seller.firstName, p.seller.lastName) FROM Product p " +
            "WHERE p.price > :fromDec AND p.price < :toDec AND p.buyer IS NULL " +
            "ORDER BY p.price ASC")
    List<ProductWithoutBuyerDTO> findAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(BigDecimal fromDec, BigDecimal toDec);

    @Query("SELECT new com.example.tryy.productShop.entities.categories.CategoryStats(" +
            "c.name, COUNT(p), avg(p.price), sum(p.price)) " +
            " FROM Product p" +
            " JOIN p.categories c" +
            " GROUP BY c ")
    List<CategoryStats> getCategoryStats();

//    @Query("SELECT new com.example.tryy.productShop.entities.categories.CategoryStats(" +
//            " c.name, COUNT(p), AVG(p.price), SUM(p.price))" +
//            " FROM Product p" +
//            " JOIN p.categories c" +
//            " GROUP BY c ")
//    List<CategoryStats> getCategoryStats();
}
