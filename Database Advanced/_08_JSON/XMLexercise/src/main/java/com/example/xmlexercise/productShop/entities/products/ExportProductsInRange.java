package com.example.xmlexercise.productShop.entities.products;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportProductsInRange {

    @XmlElement(name = "product")
    List<ProductWithAttributesDTO> products;

    public ExportProductsInRange() {}

    public ExportProductsInRange(List<ProductWithAttributesDTO> products) {
        this.products = products;
    }

    public List<ProductWithAttributesDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductWithAttributesDTO> products) {
        this.products = products;
    }
}
