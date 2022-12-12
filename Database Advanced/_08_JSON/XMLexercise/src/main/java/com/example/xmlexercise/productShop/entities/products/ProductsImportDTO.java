package com.example.xmlexercise.productShop.entities.products;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductsImportDTO {

    @XmlElement(name = "product")
    List<SingleProductImportDTO> products;

    public ProductsImportDTO() {}

    public List<SingleProductImportDTO> getProducts() {
        return products;
    }
}
