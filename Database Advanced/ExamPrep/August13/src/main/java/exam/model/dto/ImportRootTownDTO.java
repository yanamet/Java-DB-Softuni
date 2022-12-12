package exam.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "towns")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRootTownDTO {

    @XmlElement(name = "town")
    List<ImportTownDTO> towns;

    public List<ImportTownDTO> getTowns() {
        return towns;
    }

    public void setTowns(List<ImportTownDTO> towns) {
        this.towns = towns;
    }
}
