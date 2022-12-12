package com.example.xmlexercise.productShop.entities.products;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class SingleProductImportDTO {

    @XmlElement
    private String name;

    @XmlElement
    private BigDecimal price;

    public SingleProductImportDTO() {}

    public String getName() {
        return name;
    }



    public BigDecimal getPrice() {
        return price;
    }


}
