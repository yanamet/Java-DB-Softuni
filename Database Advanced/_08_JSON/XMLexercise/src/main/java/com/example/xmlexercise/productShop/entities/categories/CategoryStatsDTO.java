package com.example.xmlexercise.productShop.entities.categories;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement(name = "category")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryStatsDTO {

    @XmlAttribute(name = "name")
    private String category;

    @XmlElement(name = "product-count")
    private long productCount;

    @XmlElement(name = "average-price")
    private double averagePrice;

    @XmlElement(name = "total-revenue")
    private BigDecimal totalRevenue;

    public CategoryStatsDTO(String category, long productCount, double averagePrice, BigDecimal totalRevenue) {
        this.category = category;
        this.productCount = productCount;
        this.averagePrice = averagePrice;
        this.totalRevenue = totalRevenue;
    }

    public CategoryStatsDTO() {}

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
