package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRootCarDTO {

    @XmlElement(name = "car")
    List<ImportCarDTO> cars;

    public List<ImportCarDTO> getCars() {
        return cars;
    }
}
