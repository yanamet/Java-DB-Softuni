package hiberspring.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRootProductDTO {

    @XmlElement(name = "product")
    List<ImportProductDTO> products;

    public List<ImportProductDTO> getProducts() {
        return products;
    }

    public ImportRootProductDTO() {}

    public void setProducts(List<ImportProductDTO> products) {
        this.products = products;
    }
}
