package softuni.exam.domain.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTeamDTO {


    @XmlElement
    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    @XmlElement
    @NotNull
    private  ImportPictureDTO picture;

    public String getName() {
        return name;
    }

    public ImportPictureDTO getPicture() {
        return picture;
    }
}
