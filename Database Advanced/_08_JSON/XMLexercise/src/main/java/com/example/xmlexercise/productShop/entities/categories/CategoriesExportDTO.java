package com.example.xmlexercise.productShop.entities.categories;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoriesExportDTO {

    @XmlElement(name = "category")
    List<CategoryStatsDTO> categories;

    public CategoriesExportDTO(List<CategoryStatsDTO> categories) {
        this.categories = categories;
    }

    public List<CategoryStatsDTO> getCategories() {
        return categories;
    }

    public CategoriesExportDTO() {}
}
