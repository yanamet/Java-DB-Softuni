package com.example.xmlexercise.productShop.entities.users;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportSellersDTO {

    @XmlElement(name = "user")
    List<ExportUsersWithSoldItemsDTO> users;

    public ExportSellersDTO() {}

    public ExportSellersDTO(List<ExportUsersWithSoldItemsDTO> users) {
        this.users = users;
    }

    public List<ExportUsersWithSoldItemsDTO> getUsers() {
        return users;
    }


}
