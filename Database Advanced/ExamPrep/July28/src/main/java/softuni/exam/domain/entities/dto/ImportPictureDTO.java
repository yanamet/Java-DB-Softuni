package softuni.exam.domain.entities.dto;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPictureDTO {
    @XmlElement(name = "url")
    private String url;

    public String getUrl() {
        return url;
    }

    public ImportPictureDTO() {}

    public void setUrl(String url) {
        this.url = url;
    }
}
