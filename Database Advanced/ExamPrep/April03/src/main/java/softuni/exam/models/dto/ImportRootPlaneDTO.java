package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportRootPlaneDTO {

    @XmlElement(name = "plane")
    List<ImportPlaneDTO> planes;

    public List<ImportPlaneDTO> getPlanes() {
        return planes;
    }

}
