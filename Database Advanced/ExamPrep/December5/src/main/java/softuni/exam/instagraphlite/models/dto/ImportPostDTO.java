package softuni.exam.instagraphlite.models.dto;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "post")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPostDTO {

    @XmlElement(name = "caption")
    @Size(min = 21)
    private String caption;

    @XmlElement(name = "user")
    private UserDTO user;

    @XmlElement(name = "picture")
    private PicturePathDTO picture;

    public String getCaption() {
        return caption;
    }

    public UserDTO getUser() {
        return user;
    }

    public PicturePathDTO getPicture() {
        return picture;
    }
}
