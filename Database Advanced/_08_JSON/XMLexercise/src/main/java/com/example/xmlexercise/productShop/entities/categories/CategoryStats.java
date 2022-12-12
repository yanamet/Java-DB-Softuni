package com.example.xmlexercise.productShop.entities.categories;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

public class CategoryStats {
    private String category;

    private long productCount;

    private double averagePrice;

    private BigDecimal totalRevenue;

    public CategoryStats(String category, long productCount, double averagePrice, BigDecimal totalRevenue) {
        this.category = category;
        this.productCount = productCount;
        this.averagePrice = averagePrice;
        this.totalRevenue = totalRevenue;
    }

    public CategoryStats() {}

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProductCount(long productCount) {
        this.productCount = productCount;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getCategory() {
        return category;
    }
    public long getProductCount() {
        return productCount;
    }
    public double getAveragePrice() {
        return averagePrice;
    }
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
}
